package de.twisted.examshare.ui.commentcommon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import de.twisted.examshare.data.models.Comment
import de.twisted.examshare.databinding.FragmentAddEditCommentBinding
import de.twisted.examshare.ui.account.setProfileAvatar
import de.twisted.examshare.util.extensions.addTextChangeListener
import timber.log.Timber

class AddEditCommentFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddEditCommentBinding

    private var comment: Comment? = null
    private var authorId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        comment = arguments?.getParcelable(ARG_COMMENT)
        authorId = arguments?.getString(ARG_AUTHOR_ID)

        if (comment == null && authorId == null) {
            Timber.e("Neither existing comment nor user id specified")
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddEditCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setProfileAvatar(
            requireContext(),
            binding.commentAuthorProfileImage,
            comment?.authorId ?: authorId ?: ""
        )

        binding.commentMessage.apply {
            setText(comment?.message)
            addTextChangeListener {
                binding.commentSubmitButton.isVisible = !it.isBlank()
            }
        }

        binding.commentSubmitButton.setOnClickListener {
            (parentFragment as Listener).onCommentSubmitted(binding.commentMessage.text.toString())
        }
    }

    interface Listener {
        fun onCommentSubmitted(message: String)
    }

    companion object {
        private const val ARG_COMMENT = "comment"
        private const val ARG_AUTHOR_ID = "authorId"

        fun newInstance(authorId: String?, comment: Comment? = null) = AddEditCommentFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_COMMENT, comment)
                putString(ARG_AUTHOR_ID, authorId)
            }
        }
    }
}