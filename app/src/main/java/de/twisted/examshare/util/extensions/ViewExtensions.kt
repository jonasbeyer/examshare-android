package de.twisted.examshare.util.extensions

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import de.twisted.examshare.R
import de.twisted.examshare.ui.shared.widgets.OverlayProgressBar
import de.twisted.examshare.util.result.Event
import de.twisted.examshare.util.result.EventObserver

fun Context.setUpOverlayProgressBar(isLoading: LiveData<Boolean>, lifecycleOwner: LifecycleOwner) {
    val overlayProgressBar = OverlayProgressBar(this)
    isLoading.observe(lifecycleOwner, Observer {
        overlayProgressBar.setVisible(it)
    })
}

fun setUpErrorSnackbar(
    errorMessage: LiveData<Event<Int>>,
    view: View,
    lifecycleOwner: LifecycleOwner
) {
    errorMessage.observe(lifecycleOwner, EventObserver {
        view.showSnackbar(it)
    })
}

fun View.showSnackbar(@StringRes resId: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, resId, duration).show()
}

fun Context.showToast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, resId, duration).show()
}

fun ViewPager.addOnPageSelectedListener(callback: (position: Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        override fun onPageSelected(position: Int) {
            callback(position)
        }
    })
}

fun ViewPager2.registerOnPageSelectedCallback(callback: (position: Int) -> Unit) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            callback(position)
        }
    })
}

fun EditText.addTextChangeListener(callback: (text: String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            callback(s.toString())
        }
    })
}

fun ViewPager2.setUpWithTabLayout(
    tabLayout: TabLayout,
    titleCallback: (position: Int) -> CharSequence
) {
    TabLayoutMediator(tabLayout, this) { tab, position ->
        tab.text = titleCallback(position)
    }.attach()
}

fun TabLayout.addOnTabSelectedListener(callback: (tab: TabLayout.Tab) -> Unit) {
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab) {}
        override fun onTabUnselected(tab: TabLayout.Tab) {}
        override fun onTabSelected(tab: TabLayout.Tab) {
            callback(tab)
        }
    })
}

fun Spinner.addItemSelectedListener(callback: (position: Int) -> Unit) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {}
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            callback(position)
        }
    }
}

fun SearchView.addOnQueryTextChangeListener(callback: (queryText: String) -> Unit) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            callback(newText)
            return true
        }
    })
}

val AlertDialog.selectedItem: String
    get() = listView.adapter.getItem(listView.checkedItemPosition) as String