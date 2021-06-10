package de.twisted.examshare.ui.commentdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import de.twisted.examshare.R
import de.twisted.examshare.data.models.Comment
import de.twisted.examshare.data.models.Task
import de.twisted.examshare.databinding.ActivityCommentDetailsBinding
import de.twisted.examshare.ui.shared.ViewModelFactory
import de.twisted.examshare.ui.shared.base.ExamActivity
import de.twisted.examshare.ui.commentcommon.CommentListAdapter
import de.twisted.examshare.util.result.EventObserver
import timber.log.Timber
import javax.inject.Inject

class CommentDetailsActivity : ExamActivity() {
    private val task: Task? = null
    private val comment: Comment? = null
    private val deleted = false
    private val message = false
    private val listAdapter: CommentListAdapter? = null

    private lateinit var binding: ActivityCommentDetailsBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<CommentDetailsViewModel>

    private val viewModel: CommentDetailsViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comment_details)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        val task = intent.getParcelableExtra<Task>("task")

        val comment = intent.getParcelableExtra<Comment>(EXTRA_COMMENT)
        if (comment == null) {
            Timber.e("Comment parcelable not specified")
            finish()
        } else {
            viewModel.setParentComment(comment)
        }

        viewModel.navigateToCommentAnswers.observe(this, EventObserver {
            openCommentDetails(it.first, it.second)
        })
    }

    private fun openCommentDetails(comment: Comment, answer: Boolean) {
        val intent = newIntent(this, comment, answer)
        startActivity(intent)
    }

    // @Override
// protected void onLoad(Bundle bundle) {
//     this.listAdapter = new CommentListAdapter(this, task, comment);
//
//     listView.setAdapter(listAdapter);
//     listView.setOnItemClickListener(listAdapter.getClickListener());
//
//     refreshLayout.setOnRefreshListener(() -> loadComments(() -> refreshLayout.setRefreshing(false)));
//
//     loadComments(() -> {
//         refreshLayout.setEnabled(true);
//         progressBar.setVisibility(View.GONE);
//         if (bundle.containsKey("answer"))
//             listAdapter.showCommentEditor(null);
//     });
// }
//
// private void loadComments(Runnable executeable) {
//     commentManager.loadComments(task.getId(), comment.getId(), comments -> {
//         listAdapter.setItemList(comment, comments);
//         executeable.run();
//     });
// }
//
// public void delete(boolean message) {
//     this.deleted = true;
//     this.message = message;
//     this.onBackPressed();
// }
//
// @Override
// protected void onNewIntent(Intent intent) {
//     Comment newComment = intent.getParcelableExtra("comment");
//     if (newComment.getId() != comment.getId()) {
//         intent.setFlags(0);
//         startActivity(intent);
//     }
// }
//
//

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_comment_details, menu)
        return super.onCreateOptionsMenu(menu)
    }
//
// @Override
// public boolean onOptionsItemSelected(MenuItem item) {
//     if (item.getItemId() == R.id.addComment) {
//         if (listAdapter.isLoaded())
//             listAdapter.showCommentEditor(null);
//     }
//     return super.onOptionsItemSelected(item);
// }
//
// @Override
// public void onBackPressed() {
//     Intent intent = new Intent();
//     intent.putExtra("comment", comment);
//     intent.putExtra("deleted", deleted);
//     intent.putExtra("message", message);
//     setResult(Activity.RESULT_OK, intent);
//     super.onBackPressed();
// }
//
// @Override
// protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//     super.onActivityResult(requestCode, resultCode, data);
//     if (requestCode != 2 || resultCode != Activity.RESULT_OK) return;
//     if (data.hasExtra("finish")) finishStack();
//     if (data.hasExtra("comment")) {
//         Comment comment = data.getParcelableExtra("comment");
//         listAdapter.changeComment(comment, data.getExtras());
//     }
// }

    companion object {
        private const val EXTRA_COMMENT = "comment"
        private const val EXTRA_EDITOR_VISIBLE = "editor_visible"

        fun newIntent(context: Context, comment: Comment, editorVisible: Boolean = false): Intent {
            return Intent(context, CommentDetailsActivity::class.java).apply {
                putExtra(EXTRA_COMMENT, comment)
                putExtra(EXTRA_EDITOR_VISIBLE, editorVisible)
            }
        }
    }
}