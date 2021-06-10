package de.twisted.examshare.ui.taskdetails

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import de.twisted.examshare.R
import de.twisted.examshare.databinding.DialogTaskRatingBinding
import de.twisted.examshare.ui.shared.AsyncDialogBuilder

class TaskRatingDialogFragment : DialogFragment() {

    private lateinit var binding: DialogTaskRatingBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AsyncDialogBuilder(requireContext())
            .setPositiveButtonAsync(R.string.rate) {}
            .setNegativeButton(R.string.skip, null)
            .setTitle(R.string.rate_task)
            .create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogTaskRatingBinding.inflate(layoutInflater, container, false)
        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, _ ->
            if (rating < 1) {
                ratingBar.rating = 1.0F
            }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (showsDialog) {
            (requireDialog() as AlertDialog).setView(binding.root)
        }
    }

//
//     DialogBuilder builder = new DialogBuilder(this, R.string.rate_task, rootView);
//     builder.setNegativeButton(R.string.skip, () -> finish());
//     builder.setPositiveButton(R.string.rate, dialog -> {
//         dialog.switchButtons(false);
//         taskManager.rateTask(task, (int) ratingBar.getRating(), (response) -> {
//             if (response.isDenied()) {
//                 dialog.switchButtons(true);
//                 return;
//             }
//             dialog.dismiss();
//             if (response.getType() == ResponseType.NOT_FOUND) {
//                 TextUtil.toast(this, R.string.task_not_exist);
//                 this.notifyTaskChange(task, UpdateStatus.REMOVED);
//                 this.finish();
//                 return;
//             }
//             task.rate(response.getDouble("rating"));
//             this.notifyTaskChange(task, UpdateStatus.CHANGED);
//             this.onBackPressed();
//         });
//     });
//     builder.show();
//     dialogShown = true;

    companion object {
        fun newInstance(): TaskRatingDialogFragment = TaskRatingDialogFragment()
    }
}