package de.twisted.examshare.ui.taskdetails.images

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import dagger.android.support.DaggerFragment
import de.twisted.examshare.R
import de.twisted.examshare.data.models.Task
import de.twisted.examshare.databinding.FragmentTaskDetailsImagesBinding
import de.twisted.examshare.ui.taskdetails.TaskFullImageActivity
import de.twisted.examshare.util.ImageLibrary
import de.twisted.examshare.util.ImageType

class TaskImagesFragment : Fragment() {

    private lateinit var binding: FragmentTaskDetailsImagesBinding
    private lateinit var imagePageAdapter: TaskImagePageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskDetailsImagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val task = arguments?.getParcelable<Task>(ARG_TASK)
        val type = arguments?.getString(ARG_TYPE)

        if (task != null && type != null) {
            imagePageAdapter = TaskImagePageAdapter(task, ImageType.valueOf(type))
                .apply { imageLoadedListener = (activity as Listener) }

            binding.viewPager.adapter = imagePageAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_task_details, menu)

        menu.findItem(R.id.menu_item_add_comment).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_item_open_fullscreen -> {
                openCurrentImageInFullscreen()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun openCurrentImageInFullscreen() {
        val bitmap = imagePageAdapter.bitmapList.get(binding.viewPager.currentItem) ?: return

        ImageLibrary.setBitmap(bitmap)
        startActivity(Intent(requireContext(), TaskFullImageActivity::class.java))
    }

    interface Listener {
        fun onImageLoaded()
    }

    companion object {
        private const val ARG_TASK = "task"
        private const val ARG_TYPE = "type"

        fun newInstance(task: Task, type: ImageType) = TaskImagesFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_TYPE, type.toString())
                putParcelable(ARG_TASK, task)
            }
        }
    }
}