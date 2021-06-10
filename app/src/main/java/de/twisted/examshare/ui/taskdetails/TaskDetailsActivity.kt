package de.twisted.examshare.ui.taskdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import de.twisted.examshare.R
import de.twisted.examshare.data.models.Task
import de.twisted.examshare.databinding.ActivityTaskDetailsBinding
import de.twisted.examshare.ui.shared.ViewModelFactory
import de.twisted.examshare.ui.shared.base.ExamActivity
import de.twisted.examshare.ui.taskcommon.TaskDetailsDialogFragment
import de.twisted.examshare.ui.taskdetails.images.TaskImagesFragment
import de.twisted.examshare.util.extensions.setUpWithTabLayout
import timber.log.Timber
import javax.inject.Inject

class TaskDetailsActivity : ExamActivity(), TaskImagesFragment.Listener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<TaskDetailsViewModel>

    private lateinit var task: Task
    private lateinit var binding: ActivityTaskDetailsBinding

    private val viewModel: TaskDetailsViewModel by viewModels { viewModelFactory }

    private var ratingDialogShown = false
    private var firstImagedLoadedAt = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_details)

        task = intent.getParcelableExtra(EXTRA_TASK) ?: run {
            Timber.e("Task parcelable missing")
            finish()
            return
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = task.title

        setUpViewPager(task)
    }

    private fun setUpViewPager(task: Task) {
        val fragmentAdapter = TaskDetailsFragmentAdapter(this, task)
        if (task.solutionImagesCount == 0) {
            fragmentAdapter.removeFragmentAt(1)
        }

        binding.viewPager.apply {
            adapter = fragmentAdapter
            isUserInputEnabled = false
            setUpWithTabLayout(binding.tabLayout) { getString(fragmentAdapter.getTitle(it)) }
//            registerOnPageSelectedCallback { invalidateOptionsMenu() }

        }
    }

//    private fun updateNavigationButtons(position: Int) {
//        val adapter = binding.viewPager.adapter as TaskImagePageAdapter
//        binding.apply {
//            rightNav.isVisible = adapter.hasNext(position + 1)
//            leftNav.isVisible = adapter.hasNext(position - 1)
//        }
//    }

    override fun onBackPressed() {
        if (isRatingAllowed()) {
            openTaskRatingDialog()
        } else {
            super.onBackPressed()
//            binding.pagerIndicator.releaseViewPager()
            Glide.with(applicationContext).pauseRequests()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val newTask = intent.getParcelableExtra<Task>("task")!!
        if (task.id != newTask.id) {
            intent.flags = 0
            startActivity(intent)
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_task_details, menu)
//        menu.apply {
//            findItem(R.id.menu_item_open_fullscreen).isVisible = isImageMode()
//            findItem(R.id.menu_item_add_comment).isVisible = !isImageMode()
//        }
//
//        return super.onCreateOptionsMenu(menu)
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_task_info -> {
                openTaskDetailsDialog()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onImageLoaded() {
        // Determine whether the rating dialog could be shown
        if (firstImagedLoadedAt == -1L) {
            Timber.d("First image's loading completed")
            firstImagedLoadedAt = System.currentTimeMillis()
        }
    }
//
// @Override
// protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//     super.onActivityResult(requestCode, resultCode, data);
//     if (requestCode != 2 || resultCode != Activity.RESULT_OK) return;
//     if (data.hasExtra("finish")) finish();
//     if (data.hasExtra("comment")) {
//         Comment comment = data.getParcelableExtra("comment");
//         listAdapter.changeComment(comment, data.getExtras());
//     }
// }
//

    private fun openTaskRatingDialog() {
        val instance = TaskRatingDialogFragment.newInstance()
        instance.show(supportFragmentManager, DIALOG_TASK_RATING)

        ratingDialogShown = true
    }

    private fun openTaskDetailsDialog() {
        val instance = TaskDetailsDialogFragment.newInstance(task)
        instance.show(supportFragmentManager, DIALOG_TASK_DETAILS)
    }

    private fun isRatingAllowed(): Boolean {
        val isLoaded = firstImagedLoadedAt != -1L
            && firstImagedLoadedAt + DIALOG_TASK_RATING_MIN_WATCHTIME <= System.currentTimeMillis()

        return isLoaded
            && !task.isRated
            && !task.isSubmittedBy(viewModel.getUserId())
            && !ratingDialogShown
    }

    private fun isImageMode(): Boolean {
        val tabLayout = binding.tabLayout
        val tab = tabLayout.getTabAt(tabLayout.selectedTabPosition)

        return tab?.text == getString(R.string.task_images) || tab?.text == getString(R.string.solution_images)
    }

    companion object {
        private const val EXTRA_TASK = "task"

        private const val DIALOG_TASK_DETAILS = "dialog_task_details"

        private const val DIALOG_TASK_RATING = "dialog_task_rating"
        private const val DIALOG_TASK_RATING_MIN_WATCHTIME = 12 * 1000L

        fun newIntent(context: Context, task: Task): Intent {
            return Intent(context, TaskDetailsActivity::class.java).apply {
                putExtra(EXTRA_TASK, task)
            }
        }
    }
}