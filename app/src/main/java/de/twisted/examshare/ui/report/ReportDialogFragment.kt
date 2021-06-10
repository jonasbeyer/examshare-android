package de.twisted.examshare.ui.report

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.viewModels
import dagger.android.support.DaggerDialogFragment
import de.twisted.examshare.R
import de.twisted.examshare.ui.shared.AsyncDialogBuilder
import de.twisted.examshare.ui.shared.ViewModelFactory
import de.twisted.examshare.util.extensions.selectedItem
import de.twisted.examshare.util.extensions.setUpOverlayProgressBar
import de.twisted.examshare.util.extensions.showToast
import de.twisted.examshare.util.result.EventObserver
import timber.log.Timber
import javax.inject.Inject

class ReportDialogFragment : DaggerDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<ReportViewModel>

    private val viewModel: ReportViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val itemId = arguments?.getString(ARG_ITEM_ID)
        val itemType = arguments?.getString(ARG_ITEM_TYPE)

        if (itemId == null || itemType == null) {
            dismiss()
            Timber.e("Item id or/and item type not specified")
        } else {
            viewModel.setItem(Pair(itemId, itemType))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AsyncDialogBuilder(requireContext())
            .setPositiveButtonAsync(R.string.send) { viewModel.submitReport(it.selectedItem) }
            .setNegativeButton(R.string.cancel, null)
            .setTitle(R.string.choose_reason)
            .setSingleChoiceItems(R.array.report_reasons, 0, null)
            .create()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireContext().setUpOverlayProgressBar(viewModel.isLoading, this)

        viewModel.navigateToDismissAction.observe(this, EventObserver {
            dismiss()
            (activity as? Listener)?.onReportSubmitted()
        })

        viewModel.submitReportErrorMessage.observe(this, EventObserver {
            requireContext().showToast(it)
        })
    }

    internal interface Listener {
        fun onReportSubmitted()
    }

    companion object {
        private const val ARG_ITEM_ID = "item_id"
        private const val ARG_ITEM_TYPE = "item_type"

        fun newInstance(itemId: String, itemType: ItemType) = ReportDialogFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_ITEM_ID, itemId)
                putString(ARG_ITEM_TYPE, itemType.itemType)
            }
        }
    }
}

enum class ItemType(val itemType: String) {
    TASK("task"),
    COMMENT("comment")
}