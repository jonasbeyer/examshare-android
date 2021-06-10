package de.twisted.examshare.ui.taskcommon

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerDialogFragment
import de.twisted.examshare.R
import de.twisted.examshare.ui.shared.AsyncDialogBuilder
import de.twisted.examshare.ui.shared.ViewModelFactory
import de.twisted.examshare.util.extensions.setUpOverlayProgressBar
import de.twisted.examshare.util.extensions.showToast
import de.twisted.examshare.util.result.EventObserver
import timber.log.Timber
import javax.inject.Inject

class TaskDeleteDialogFragment : DaggerDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<TaskDeleteViewModel>

    private val viewModel: TaskDeleteViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val taskId = arguments?.getString(ARG_TASK_ID)
        if (taskId == null) {
            Timber.e("Task Id not specified")
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val arguments = requireNotNull(arguments)
        val taskId = requireNotNull(arguments.getString(ARG_TASK_ID))

        return AsyncDialogBuilder(requireContext())
            .setPositiveButtonAsync(R.string.ok) { viewModel.deleteItem(taskId) }
            .setNegativeButton(R.string.no, null)
            .setTitle(R.string.delete)
            .setMessage(R.string.confirm_delete_task)
            .create()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireContext().setUpOverlayProgressBar(viewModel.isLoading, this)

        viewModel.navigateToDismissAction.observe(this, EventObserver {
            dismiss()
            (activity as? Listener)?.onTaskDeleted()
        })

        viewModel.itemDeletionError.observe(this, EventObserver {
            requireContext().showToast(it)
        })
    }

    internal interface Listener {
        fun onTaskDeleted()
    }

    companion object {
        private const val ARG_TASK_ID = "task_id"

        fun newInstance(taskId: String) = TaskDeleteDialogFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_TASK_ID, taskId)
            }
        }
    }
}