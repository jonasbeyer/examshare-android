package de.twisted.examshare.ui.tasklist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.twisted.examshare.R
import de.twisted.examshare.data.models.Task
import de.twisted.examshare.data.models.User
import de.twisted.examshare.databinding.ActivityTaskListBinding
import de.twisted.examshare.ui.shared.ViewModelFactory
import de.twisted.examshare.ui.shared.base.ExamActivity
import de.twisted.examshare.ui.shared.widgets.VerticalSpaceItemDecoration
import de.twisted.examshare.ui.taskcommon.TaskListAdapter
import de.twisted.examshare.ui.taskcommon.TaskListMode
import de.twisted.examshare.ui.taskdetails.TaskDetailsActivity
import de.twisted.examshare.util.extensions.addOnQueryTextChangeListener
import de.twisted.examshare.util.helper.TextUtil
import de.twisted.examshare.util.result.EventObserver
import javax.inject.Inject

class TaskListActivity : ExamActivity() {
// private SearchView searchView;
//
// private User userProfile;

    private lateinit var binding: ActivityTaskListBinding

    private var user: User? = null
    private var listMode: TaskListMode? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<TaskListViewModel>

    private val viewModel: TaskListViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_list)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        user = intent.getParcelableExtra(EXTRA_USER) ?: viewModel.currentUserInfo.value

        val listMode = TaskListMode.valueOf(intent.getStringExtra(EXTRA_LIST_MODE) ?: TaskListMode.TASKS.toString())
        val listAdapter = TaskListAdapter(viewModel, listMode)

        if (listMode == TaskListMode.TASKS) {
            supportActionBar?.setDisplayShowTitleEnabled(false)
            binding.spinner.apply {
                isVisible = true
                setAdapter(getSpinnerArray(listMode), null)
//              addItemSelectedListener { findTasks(false, -1) }
            }
        } else if (listMode == TaskListMode.FAVORITES) {
            supportActionBar?.title = getTitle("Test", listMode)
//          findTasks(false, -1);
        }

        setUpRecyclerView(binding.recyclerView)

        viewModel.navigateToTaskDetails.observe(this, EventObserver { task ->
            openTasksDetailsActivity(task)
        })
    }

// protected void onLoad(Bundle bundle) {
//
//     this.listView = (ExamListView) findViewById(R.id.tasks);
//     this.listView.setAdapter(listAdapter);
//     this.listView.setLoadMoreListener(lastId -> findTasks(false, lastId));
//
//     this.refreshLayout = (ExamSwipeLayout) findViewById(R.id.swipeLayout);
//     this.refreshLayout.setListView(listView);
//     this.refreshLayout.setOnRefreshListener(() -> findTasks(true, -1));
// }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.maxWidth = Integer.MAX_VALUE
        searchView.queryHint = getString(R.string.search_task)
        searchView.addOnQueryTextChangeListener {
            //            if (!searchView.isIconified) {
//                findTasks(false, -1);
//            }
        }

        val searchEditFrame = searchView.findViewById<LinearLayout>(R.id.search_edit_frame)
        (searchEditFrame.layoutParams as LinearLayout.LayoutParams).leftMargin = -12;
        return super.onCreateOptionsMenu(menu)
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = TaskListAdapter(viewModel, TaskListMode.SUBJECT)
        recyclerView.also {
            it.layoutManager = LinearLayoutManager(this)
            it.addItemDecoration(VerticalSpaceItemDecoration(this))
        }
    }

//
// private void findTasks(boolean refresh, int lastId) {
//     this.refreshLayout.setEnabled(refresh);
//     if (!refresh && lastId == -1) {
//         this.listAdapter.setItemList(Collections.emptyList());
//         this.progressBar.setVisibility(View.VISIBLE);
//         this.setTaskError(errorView, 1, false);
//     }
//     userProfile.findTasks(getKeyword(), getFilter(), lastId, (taskList, totalCount) -> {
//         int tasksCount = taskList != null ? taskList.size() : 0;
//         this.progressBar.setVisibility(View.GONE);
//         this.refreshLayout.setRefreshing(false);
//         this.listAdapter.setItemList(taskList, lastId == -1);
//         this.listView.scrollUp(lastId != -1 && taskList == null);
//         this.listView.setLoadingEnabled(tasksCount < totalCount || taskList == null && lastId != -1);
//         this.setTaskError(errorView, listAdapter.getCount(), taskList == null);
//     });
// }

    private fun getSpinnerArray(listMode: TaskListMode): Array<String> {
        val list = resources.getStringArray(R.array.search_options).toMutableList()
        list.add(0, getTitle(user?.username ?: "", listMode))

        return list.toTypedArray()
    }

    private fun getTitle(username: String, listMode: TaskListMode): String {
        val owner: String = if (viewModel.isCurrent(user?.id ?: "")) {
            getString(R.string.my)
        } else {
            TextUtil.getGenitive(username)
        }

        val title = if (listMode == TaskListMode.TASKS) {
            R.string.owners_tasks
        } else {
            R.string.owners_favorites
        }

        return getString(title, owner)
    }

    private fun openTasksDetailsActivity(task: Task) {
        val intent = TaskDetailsActivity.newIntent(this, task)
        startActivity(intent)
    }

    // private String getTitle(String username) {
//     ListMode listMode = listAdapter.getListMode();
//     String owner = userProfile.isMyProfile() ? getString(R.string.my) : TextUtil.getGenitive(username);
//     return getString(listMode == ListMode.TASKS ? R.string.owners_tasks : R.string.owners_favorites, owner);
// }
//
// private String[] getSpinnerArray() {
//     List<String> spinnerList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.search_options)));
//     spinnerList.add(0, getTitle(userProfile.getUsername()));
//     return spinnerList.toArray(new String[0]);
// }
//
// private int getFilter() {
//     return this.spinner != null ? this.spinner.getSelectedItemPosition() : 5;
// }
//
// private String getKeyword() {
//     return this.searchView != null ? this.searchView.getQuery().toString().trim() : "";
// }
//
//
// @Override
// public void onTaskListUpdate(UpdateStatus listUpdate) {
//     if (listUpdate == UpdateStatus.EMPTY)
//         setTaskError(errorView, 1, false);
//     if (listUpdate == UpdateStatus.REMOVED)
//         listView.loadIfNotScrollable(() -> findTasks(false, listAdapter.getLastItemId()));
// }

    companion object {
        private const val EXTRA_LIST_MODE = "listMode"
        private const val EXTRA_USER = "user"

        fun newIntent(context: Context, listMode: TaskListMode, user: User? = null): Intent {
            return Intent(context, TaskListActivity::class.java).apply {
                putExtra(EXTRA_USER, user)
                putExtra(EXTRA_LIST_MODE, listMode.toString())
            }
        }
    }
}