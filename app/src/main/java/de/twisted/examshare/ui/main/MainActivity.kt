package de.twisted.examshare.ui.main

import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import de.twisted.examshare.R
import de.twisted.examshare.databinding.ActivityMainBinding
import de.twisted.examshare.ui.main.category.SubjectCategoryAdapter
import de.twisted.examshare.ui.shared.ViewModelFactory
import de.twisted.examshare.ui.shared.base.ExamActivity
import javax.inject.Inject

class MainActivity : ExamActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<MainViewModel>

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setSupportActionBar(binding.toolbar)
        setUpViewPagerWithTabLayout()
    }

    private fun setUpViewPagerWithTabLayout() {
        val adapter = SubjectCategoryAdapter(supportFragmentManager)

        binding.apply {
            viewPager.adapter = adapter
            tabLayout.setupWithViewPager(viewPager)
        }

        viewModel.categories.observe(this, Observer(adapter::setCategories))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
}