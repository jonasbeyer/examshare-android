package de.twisted.examshare.ui.taskdetails

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import de.twisted.examshare.R
import de.twisted.examshare.data.models.Task
import de.twisted.examshare.ui.taskdetails.comments.TaskCommentListFragment
import de.twisted.examshare.ui.taskdetails.images.TaskImagesFragment
import de.twisted.examshare.util.ImageType

class TaskDetailsFragmentAdapter constructor(
    private val fragmentActivity: FragmentActivity,
    private val task: Task
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position].invoke()

    fun getTitle(position: Int) = fragmentTitles[position]

    fun removeFragmentAt(position: Int) {
        fragmentTitles.removeAt(position)
        fragments.removeAt(position)
    }

    private val fragmentTitles = mutableListOf(
        R.string.task_images,
        R.string.solution_images,
        R.string.comments
    )

    val fragments = mutableListOf(
        { TaskImagesFragment.newInstance(task, ImageType.TASK_IMAGE) },
        { TaskImagesFragment.newInstance(task, ImageType.SOLUTION_IMAGE) },
        { TaskCommentListFragment.newInstance(task.id) }
    )
}