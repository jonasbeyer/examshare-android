package de.twisted.examshare.ui.taskcommon

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import de.twisted.examshare.R
import de.twisted.examshare.data.models.Task
import de.twisted.examshare.databinding.DialogTaskDetailsBinding
import timber.log.Timber

class TaskDetailsDialogFragment : DialogFragment() {

    private lateinit var binding: DialogTaskDetailsBinding
    private lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        task = arguments?.getParcelable(ARG_TASK) ?: run {
            Timber.e("Missing task parcelable")
            dismiss()
            return
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(task.title)
            .setPositiveButton(R.string.close, null)
            .create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogTaskDetailsBinding.inflate(layoutInflater, container, false)
        binding.task = task
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (showsDialog) {
            (requireDialog() as AlertDialog).setView(binding.root)
        }
    }

    companion object {
        private const val ARG_TASK = "task"

        fun newInstance(task: Task) = TaskDetailsDialogFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_TASK, task)
            }
        }
    }
}