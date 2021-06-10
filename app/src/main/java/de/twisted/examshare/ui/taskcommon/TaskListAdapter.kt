package de.twisted.examshare.ui.taskcommon

import android.view.*
import android.widget.PopupMenu
import androidx.core.view.forEachIndexed
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.twisted.examshare.R
import de.twisted.examshare.data.models.Task
import de.twisted.examshare.databinding.ItemTaskBinding

class TaskListAdapter(
    private val actions: TaskActions,
    private val listMode: TaskListMode
) : ListAdapter<Task, TaskViewHolder>(TaskDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding, listMode, actions, true)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bindTask(getItem(position))
    }

// @Override
// public OnMenuItemClickListener getMenuListener(ExamModel model, int position) {
//     return (item) -> {
//         Intent intent;
//         Task task = (Task) model;
//         switch (item.getItemId()) {
//             case R.string.delete:
//                 Dialogs.confirmDelete(activity, R.string.confirm_delete_task, () -> {
//                     activity.showProgressDialog();
//                     taskManager.deleteTask(task, (response) -> {
//                         activity.getProgressDialog().dismiss();
//                         this.removeIfNotAvaiable(task, response.getType());
//                         if (response.getType() == ResponseType.SUCCESS)
//                             activity.notifyTaskChange(task, UpdateStatus.REMOVED);
//                     });
//                 });
//                 break;
//             case R.string.report:
//                 Dialogs.showReportDialog(activity, task, response -> {
//                     this.removeIfNotAvaiable(task, response.getType());
//                     if (response.getType() == ResponseType.SUCCESS)
//                         TextUtil.snackbar(activity, R.string.task_reported);
//                 });
//         }
//         return true;
//     };
// }

//    private fun removeIfNotAvaiable(task: Task, responseType: ResponseType) {
//        if (responseType == ResponseType.NOT_FOUND) {
//            activity.notifyTaskChange(task, UpdateStatus.REMOVED)
//            TextUtil.toast(activity, R.string.task_not_exist)
//        }
//    }
}

class TaskViewHolder(
    private val binding: ItemTaskBinding,
    private val listMode: TaskListMode,
    private val actions: TaskActions,
    private val moderationAllowed: Boolean
) : RecyclerView.ViewHolder(binding.root) {

    fun bindTask(task: Task) {
        binding.task = task
        binding.listMode = listMode
        binding.actions = actions
        binding.executePendingBindings()

        binding.taskOptionsMenu.setOnClickListener {
            showOptionsMenu(it, task)
        }
    }

    private fun showOptionsMenu(anchor: View, task: Task) {
        val popupMenu = PopupMenu(anchor.context, anchor, Gravity.END).apply {
            menuInflater.inflate(R.menu.menu_task_options, menu)
            menu.forEachIndexed { _, item ->
                when (item.itemId) {
                    R.id.menu_item_task_details -> item.isVisible = true
                    R.id.menu_item_task_edit,
                    R.id.menu_item_task_delete -> item.isVisible = moderationAllowed
                    else -> item.isVisible = task.author == "Twister21"
                }
            }

            setOnMenuItemClickListener {
                onOptionsMenuItemSelected(it, task)
                true
            }
        }

        popupMenu.show()
    }

    private fun onOptionsMenuItemSelected(item: MenuItem, task: Task) {
        when (item.itemId) {
            R.id.menu_item_task_details -> actions.openTaskDetailsDialog(task)
            R.id.menu_item_task_edit -> actions.openTaskEditAction(task)
            R.id.menu_item_task_delete -> actions.openConfirmTaskDeleteDialog(task)
            R.id.menu_item_task_report -> actions.openReportDialog(task)
            R.id.menu_item_task_author_profile -> actions.openAuthorProfile(task.authorId)
        }
    }
}

enum class TaskListMode {
    FAVORITES, TASKS, SUBJECT, ALL
}

object TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id;
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}