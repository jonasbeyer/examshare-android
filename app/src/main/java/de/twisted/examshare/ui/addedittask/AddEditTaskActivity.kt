package de.twisted.examshare.ui.addedittask

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.twisted.examshare.R
import de.twisted.examshare.data.models.Task
import de.twisted.examshare.databinding.ActivityAddEditTaskBinding
import de.twisted.examshare.service.task.TaskUploadService
import de.twisted.examshare.ui.shared.ViewModelFactory
import de.twisted.examshare.ui.shared.base.ExamActivity
import de.twisted.examshare.util.Preferences
import de.twisted.examshare.util.extensions.addOnTabSelectedListener
import de.twisted.examshare.util.input.InputFilterMinMax
import de.twisted.imagepicker.activities.ImagePicker
import de.twisted.imagepicker.utility.PermUtil
import timber.log.Timber
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.collections.LinkedHashMap

class AddEditTaskActivity : ExamActivity() {

    companion object {
        private const val TASKS_ID = 0
        private const val SOLUTIONS_ID = 1

        private const val EXTRA_SUBJECT_ID = "subject_id"
        private const val EXTRA_TASK = "task"

        fun newIntent(context: Context, subjectId: String, task: Task? = null): Intent {
            return Intent(context, AddEditTaskActivity::class.java).apply {
                putExtra(EXTRA_SUBJECT_ID, subjectId)
                putExtra(EXTRA_TASK, task)
            }
        }
    }

    private val inputViews = LinkedHashMap<EditText, Int>()

    private var taskImages = LinkedHashMap<String, String>()
    private var solutionImages = LinkedHashMap<String, String>()

    private lateinit var subjectId: String
    private lateinit var binding: ActivityAddEditTaskBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<AddEditViewModel>

    private val viewModel: AddEditViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subjectId = intent.getStringExtra(EXTRA_SUBJECT_ID) ?: run {
            Timber.e("Subject Id not specified")
            finish()
            return
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit_task)
        binding.task = intent.getParcelableExtra(EXTRA_TASK)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.tabLayout.apply {
            getTabAt(0)?.text = getString(R.string.task_images_count, 0);
            getTabAt(1)?.text = getString(R.string.solution_images_count, 0);
            addOnTabSelectedListener { addImagesToView(it.position) }
        }

        binding.keywords.apply {
            setTags(Collections.emptyList())
            setMaxTags(Preferences.MAX_TAGS)
            binding.keywords.setOnTagChangeListener { tagGroup, _ ->
                if (tagGroup.tags.size >= Preferences.MIN_TAGS)
                    binding.keywords.setError(false)
            }
        }

        binding.floationActionButton.setOnClickListener {
            selectImages(binding.tabLayout.selectedTabPosition)
        }

        inputViews.apply {
            put(binding.grade, 1)
            put(binding.schoolForm, 2)
            put(binding.federalState, 2)
        }

        binding.apply {
//          title.addTextChangedListener(TextUtil.getNotEmptyListener(title))

            schoolForm.setAdapter(getAutoCompleteAdapter(R.array.school_forms))
//          schoolForm.addTextChangedListener(TextUtil.getNotEmptyListener(schoolForm));

            federalState.setAdapter(getAutoCompleteAdapter(R.array.federal_states))
//          federalState.addTextChangedListener(TextUtil.getNotEmptyListener(federalState));

            grade.filters = arrayOf(InputFilterMinMax(1, 13))
//          grade.addTextChangedListener(TextUtil.getNotEmptyListener(grade));

            layout.isFocusableInTouchMode = true
            layout.requestFocus()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_edit_task, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_done -> {
            startTaskUploadService()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun startTaskUploadService() {
        val taskFiles = getFiles(taskImages.values)
        val solutionFiles = getFiles(solutionImages.values)

        with(binding) {
            val task = Task(subjectId, binding.title.text.toString(), binding.keywords.tags.toList())

            task.taskFiles = taskFiles
            task.solutionFiles = solutionFiles
            task.grade = grade.text.toString().toInt()
            task.creator = creator.text.toString()
            task.schoolForm = schoolForm.text.toString()
            task.federalState = federalState.text.toString()

            val intent = TaskUploadService.newInstance(this@AddEditTaskActivity, task)

            startService(intent)
            finish()
        }
    }

    private fun getFiles(uriList: Collection<String>): List<File> {
        return uriList.mapNotNull { uri -> File(uri).takeIf { it.exists() } }
    }

    private fun selectImages(id: Int) {
        val selectionCount = if (id == TASKS_ID) Preferences.MAX_TASK_IMAGES else Preferences.MAX_SOLUTION_IMAGES
        val uriSet = if (id == TASKS_ID) taskImages.keys else solutionImages.keys

        ImagePicker.start(this, ArrayList(uriSet), id, selectionCount)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS)
            return

        for (i in 0 until permissions.size) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                return
        }

        selectImages(binding.tabLayout.selectedTabPosition)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK)
            return

        val typeToken = object : TypeToken<LinkedHashMap<String, String>>() {}.type

        when (requestCode) {
            TASKS_ID -> {
                taskImages = Gson().fromJson(data?.getStringExtra(ImagePicker.IMAGE_RESULTS)
                    ?: "", typeToken)
                binding.tabLayout.getTabAt(requestCode)?.text = getString(R.string.task_images_count, taskImages.size)
                addImagesToView(TASKS_ID)
//                validateImages()
            }
            SOLUTIONS_ID -> {
                solutionImages = Gson().fromJson(data?.getStringExtra(ImagePicker.IMAGE_RESULTS)
                    ?: "", typeToken)
                binding.tabLayout.getTabAt(requestCode)?.text = getString(R.string.solution_images_count, solutionImages.size)
                addImagesToView(SOLUTIONS_ID)
            }
        }
    }

    private fun addImagesToView(position: Int) {
        val paths = if (position == TASKS_ID) {
            taskImages.values
        } else {
            solutionImages.values
        }

        val adapter = ImageGridAdapter(this, paths.toList())
        val layoutManager = StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL)
            .apply { gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS }

        binding.recyclerView.also {
            it.layoutManager = layoutManager
            it.adapter = adapter
            it.itemAnimator = DefaultItemAnimator()
        }
    }

// @Override
// public boolean onInputCheck(boolean valid) {
//     if (TextUtil.isEmpty(title)) return false;
//     if (keywords.getTags().length < Preferences.MIN_TAGS) {
//         keywords.setError(true);
//         spinner.setSelection(0);
//         TextUtil.toast(this, R.string.two_keywords_required);
//         return false;
//     }
//     for (EditText view : inputViews.keySet()) {
//         int spinnerPos = inputViews.get(view);
//         if (TextUtil.isEmpty(view)) {
//             spinner.setSelection(spinnerPos);
//             return false;
//         }
//     }
//     return validateImages();
// }
//
// private boolean validateImages() {
//     if (taskImages.size() == 0) {
//         tabLayout.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.holo_red_dark));
//         tabLayout.getTabAt(0).select();
//         TextUtil.toast(this, R.string.one_task_image_required);
//         return false;
//     }
//     tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
//     return true;
// }
}