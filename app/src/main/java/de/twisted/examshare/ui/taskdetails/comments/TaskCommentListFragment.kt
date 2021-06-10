package de.twisted.examshare.ui.taskdetails.comments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import de.twisted.examshare.R
import de.twisted.examshare.data.models.Comment
import de.twisted.examshare.databinding.FragmentTaskDetailsCommentListBinding
import de.twisted.examshare.ui.account.AccountActivity
import de.twisted.examshare.ui.commentcommon.AddEditCommentFragment
import de.twisted.examshare.ui.commentcommon.CommentListAdapter
import de.twisted.examshare.ui.commentdetails.CommentDetailsActivity
import de.twisted.examshare.ui.shared.ViewModelFactory
import de.twisted.examshare.ui.shared.widgets.VerticalSpaceItemDecoration
import de.twisted.examshare.util.result.EventObserver
import timber.log.Timber
import javax.inject.Inject

class TaskCommentListFragment : DaggerFragment(), AddEditCommentFragment.Listener {

    private lateinit var binding: FragmentTaskDetailsCommentListBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<TaskCommentListViewModel>

    private val commentListViewModel: TaskCommentListViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskDetailsCommentListBinding.inflate(inflater, container, false)
        binding.viewModel = commentListViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val taskId = arguments?.getString(ARG_TASK_ID)
        if (taskId != null) {
            commentListViewModel.setTaskId(taskId)
        } else {
            Timber.e("Task Id not specified")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_task_details, menu)

        menu.findItem(R.id.menu_item_open_fullscreen).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_add_comment -> {
                openCommentEditorDialog(null)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView(binding.recyclerView)

        commentListViewModel.navigateToCommentAnswers.observe(
            viewLifecycleOwner,
            EventObserver { (comment, answer) -> openCommentDetailsActivity(comment, answer) }
        )

        commentListViewModel.navigateToAddEditCommentAction.observe(
            viewLifecycleOwner,
            EventObserver { comment -> openCommentEditorDialog(comment) }
        )

        commentListViewModel.navigateToCommentAuthorProfile.observe(
            viewLifecycleOwner,
            EventObserver { userId -> openCommentAuthorProfileActivity(userId) }
        )

        commentListViewModel.snackbarMessage.observe(
            viewLifecycleOwner,
            EventObserver { message -> Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)}
        )
    }

    override fun onCommentSubmitted(message: String) = commentListViewModel.submitComment(message)

    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = CommentListAdapter(null, commentListViewModel)
        recyclerView.also {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.addItemDecoration(DividerItemDecoration(requireContext(), VERTICAL))
        }
    }

    private fun openCommentEditorDialog(comment: Comment?) {
        AddEditCommentFragment.newInstance(commentListViewModel.getUserId(), comment)
            .show(childFragmentManager, DIALOG_COMMENT_EDITOR)
    }

    private fun openCommentDetailsActivity(comment: Comment, editorVisible: Boolean) {
        startActivity(CommentDetailsActivity.newIntent(requireContext(), comment, editorVisible))
    }

    private fun openCommentAuthorProfileActivity(userId: String) {
        startActivity(AccountActivity.newIntent(requireContext(), userId))
    }

    companion object {
        private const val ARG_TASK_ID = "taskId"
        private const val DIALOG_COMMENT_EDITOR = "dialog_comment_editor"

        fun newInstance(taskId: String) = TaskCommentListFragment().apply {
            arguments = Bundle().apply { putString(ARG_TASK_ID, taskId) }
        }
    }
}