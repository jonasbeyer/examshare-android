package de.twisted.examshare.ui.main.category

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import de.twisted.examshare.ui.main.subjectlist.SubjectListFragment

class SubjectCategoryAdapter(fragmentManager: FragmentManager)
    : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var categories: List<String> = emptyList()

    override fun getCount(): Int {
        return categories.size
    }

    override fun getItem(position: Int): Fragment {
        return SubjectListFragment.newInstance(categories[position])
    }

    fun setCategories(categories: List<String>) {
        this.categories = categories
        notifyDataSetChanged()
    }

//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//        val binding = FragmentSubjectListBinding.inflate(LayoutInflater.from(container.context), container, false)
//        val recyclerView = binding.recyclerView
//        val category = getPageTitle(position).toString()
//
//        recyclerView.adapter = SubjectListAdapter(object: SubjectActions {
//            override fun openSubjectDetails(subject: Subject) {}
//            override fun updateNotificationPreference(subject: Subject) {}
//        })
//
//        container.addView(binding.root)
//        return binding.root
//    }

    override fun getPageTitle(position: Int): CharSequence? {
        return categories[position]
    }
}