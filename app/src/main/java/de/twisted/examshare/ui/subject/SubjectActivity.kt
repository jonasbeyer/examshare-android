package de.twisted.examshare.ui.subject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import de.twisted.examshare.R
import de.twisted.examshare.data.models.Subject
import de.twisted.examshare.data.models.Task
import de.twisted.examshare.databinding.ActivitySubjectBinding
import de.twisted.examshare.ui.account.AccountActivity
import de.twisted.examshare.ui.addedittask.AddEditTaskActivity
import de.twisted.examshare.ui.report.ItemType
import de.twisted.examshare.ui.report.ReportDialogFragment
import de.twisted.examshare.ui.shared.ViewModelFactory
import de.twisted.examshare.ui.shared.base.ExamActivity
import de.twisted.examshare.ui.shared.widgets.VerticalSpaceItemDecoration
import de.twisted.examshare.ui.taskcommon.TaskDeleteDialogFragment
import de.twisted.examshare.ui.taskcommon.TaskDetailsDialogFragment
import de.twisted.examshare.ui.taskcommon.TaskListAdapter
import de.twisted.examshare.ui.taskcommon.TaskListMode
import de.twisted.examshare.ui.taskdetails.TaskDetailsActivity
import de.twisted.examshare.util.extensions.showSnackbar
import de.twisted.examshare.util.result.EventObserver
import timber.log.Timber
import javax.inject.Inject

class SubjectActivity : ExamActivity(), ReportDialogFragment.Listener, TaskDeleteDialogFragment.Listener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<SubjectViewModel>

    lateinit var binding: ActivitySubjectBinding

    private val viewModel: SubjectViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subject)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val subject = intent.getParcelableExtra<Subject>(EXTRA_SUBJECT)
        if (subject == null) {
            Timber.e("Subject not specified")
            finish()
        } else {
            viewModel.setSubjectId(subject.id)
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = subject?.name

        setUpRecyclerView(binding.recyclerView)
        observeLiveDataChanges()
    }

    private fun observeLiveDataChanges() = with(viewModel) {
        val owner = this@SubjectActivity

        navigateToTaskDetailsActivity.observe(owner, EventObserver(::openTasksDetailsActivity))
        navigateToTaskDetailsDialog.observe(owner, EventObserver(::openTasksDetailsDialog))
        navigateToReportTaskDialog.observe(owner, EventObserver(::openTaskReportDialog))
        navigateToDeleteTaskDialog.observe(owner, EventObserver(::openTaskDeleteDialog))
        navigateToTaskAuthorProfile.observe(owner, EventObserver(::openTaskAuthorProfile))

        navigateToAddEditTaskAction.observe(owner, EventObserver { pair ->
            openAddTaskActivity(pair.first, pair.second)
        })

        snackbarMessage.observe(owner, EventObserver { message ->
           binding.root.showSnackbar(message)
        })
    }

// this.listView.setAdapter(listAdapter);
// this.listView.setLoadMoreListener(lastId -> findTasks(false, lastId));
//

// this.refreshLayout.setListView(listView);
// this.refreshLayout.setOnRefreshListener(() -> findTasks(true, -1));
//

// this.searchView.init(this, () -> findTasks(false, -1));
// this.searchView.setText(bundle.getString("keyword", ""));

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // private void findTasks(boolean refresh, int lastId) {
//     this.refreshLayout.setEnabled(refresh);
//     if (!refresh && lastId == -1) {
//         this.listAdapter.setItemList(Collections.emptyList());
//         this.progressBar.setVisibility(View.VISIBLE);
//         this.setTaskError(errorView, 1, false);
//     }
//     taskManager.findTasks(subject, searchView.getQuery(), lastId, setUpListView(lastId == -1));
// }

// private BiConsumer<List<ExamModel>, Integer> setUpListView(boolean init) {
//     return (taskList, totalCount) -> {
//         int tasksCount = taskList != null ? taskList.size() : 0;
//         this.progressBar.setVisibility(View.GONE);
//         this.refreshLayout.setRefreshing(false);
//         this.listAdapter.setItemList(taskList, init);
//         this.listView.scrollUp(!init && taskList == null);
//         this.listView.setLoadingEnabled(tasksCount < totalCount || taskList == null && !init);
//         this.setTaskError(errorView, listAdapter.getCount(), taskList == null);
//     };
// }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("keyword", binding.searchView.keyword)
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView) = with(recyclerView) {
        adapter = TaskListAdapter(viewModel, TASK_LIST_MODE)
        layoutManager = LinearLayoutManager(this@SubjectActivity)
        (itemAnimator as DefaultItemAnimator).apply {
            supportsChangeAnimations = false
        }

        addItemDecoration(VerticalSpaceItemDecoration(this@SubjectActivity))
    }


    override fun onReportSubmitted() {
       binding.root.showSnackbar(R.string.task_reported)
    }

    override fun onTaskDeleted() {
        binding.root.showSnackbar(R.string.task_deleted)
    }

    private fun openAddTaskActivity(subjectId: String, task: Task?) {
        val intent = AddEditTaskActivity.newIntent(this, subjectId, task)
        startActivity(intent)
    }

    private fun openTasksDetailsActivity(task: Task) {
        val intent = TaskDetailsActivity.newIntent(this, task)
        startActivity(intent)
    }

    private fun openTasksDetailsDialog(task: Task) {
        val instance = TaskDetailsDialogFragment.newInstance(task)
        instance.show(supportFragmentManager, DIALOG_TASK_DETAILS)
    }

    private fun openTaskDeleteDialog(taskId: String) {
        val instance = TaskDeleteDialogFragment.newInstance(taskId)
        instance.show(supportFragmentManager, DIALOG_DELETE_TASK)
    }

    private fun openTaskReportDialog(taskId: String) {
        val instance = ReportDialogFragment.newInstance(taskId, ItemType.TASK)
        instance.show(supportFragmentManager, DIALOG_REPORT_TASK)
    }

    private fun openTaskAuthorProfile(authorId: String) {
        val intent = AccountActivity.newIntent(this, authorId)
        startActivity(intent)
    }

    companion object {
        private val TASK_LIST_MODE = TaskListMode.SUBJECT

        private const val EXTRA_SUBJECT = "subject"

        private const val DIALOG_TASK_DETAILS = "dialog_task_details"
        private const val DIALOG_DELETE_TASK = "dialog_delete_task"
        private const val DIALOG_REPORT_TASK = "dialog_report_task"

        fun newIntent(context: Context, subject: Subject): Intent {
            return Intent(context, SubjectActivity::class.java).apply {
                putExtra(EXTRA_SUBJECT, subject)
            }
        }
    }

//    override fun onTaskListUpdate(listUpdate: UpdateStatus) {
//        if (listUpdate == UpdateStatus.ADDED || listUpdate == UpdateStatus.EMPTY) setTaskError(errorView, listAdapter!!.count, false)
//        if (listUpdate == UpdateStatus.REMOVED) listView!!.loadIfNotScrollable(Runnable { findTasks(false, listAdapter!!.lastItemId) })
//    }
}