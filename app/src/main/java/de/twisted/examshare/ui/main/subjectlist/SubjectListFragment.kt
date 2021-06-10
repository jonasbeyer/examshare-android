package de.twisted.examshare.ui.main.subjectlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import de.twisted.examshare.data.models.Subject
import de.twisted.examshare.databinding.FragmentSubjectListBinding
import de.twisted.examshare.ui.shared.ViewModelFactory
import de.twisted.examshare.ui.subject.SubjectActivity
import de.twisted.examshare.util.extensions.showSnackbar
import de.twisted.examshare.util.result.EventObserver
import timber.log.Timber
import javax.inject.Inject

class SubjectListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<SubjectListViewModel>

    private lateinit var binding: FragmentSubjectListBinding

    private val subjectListViewModel: SubjectListViewModel by viewModels { viewModelFactory }

    override fun onStart() {
        super.onStart()
        val category = arguments?.getString(ARG_CATEGORY)
        if (category == null) {
            Timber.e("Category name not specified")
        } else {
            subjectListViewModel.setCategory(category)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSubjectListBinding.inflate(inflater, container, false)
        binding.viewModel = subjectListViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpRecyclerView(binding.recyclerView)

        subjectListViewModel.navigateToSubjectDetails.observe(
            viewLifecycleOwner,
            EventObserver { subject ->
                openSubjectActivity(subject)
            }
        )

        subjectListViewModel.notificationUpdated.observe(
            viewLifecycleOwner,
            EventObserver { snackbarMessage ->
                binding.root.showSnackbar(snackbarMessage)
            }
        )
    }

    private fun openSubjectActivity(subject: Subject) {
        startActivity(SubjectActivity.newIntent(requireContext(), subject))
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = SubjectListAdapter(subjectListViewModel)
    }

    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: String) = SubjectListFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_CATEGORY, category)
            }
        }
    }
}
