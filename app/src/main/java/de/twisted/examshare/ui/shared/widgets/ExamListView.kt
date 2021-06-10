package de.twisted.examshare.ui.shared.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.ProgressBar
import de.twisted.examshare.R
import de.twisted.examshare.ui.shared.base.ExamListAdapter

class ExamListView : ListView {
    private var progressBar: ProgressBar? = null
    private var listAdapter: ExamListAdapter? = null
    private var loading = false
    private var loadingEnabled = false

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun setAdapter(adapter: ListAdapter) {
        super.setAdapter(adapter)
        listAdapter = adapter as ExamListAdapter
        this.onItemClickListener = listAdapter!!.clickListener
    }

    fun setLoadMoreListener(consumer: (lastItemId: Int) -> Unit) {
        loadingEnabled = true
        progressBar = inflateProgressBar()
        setOnScrollListener(object : OnScrollListener {
            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                val lastInScreen = firstVisibleItem + visibleItemCount
                if (lastInScreen >= totalItemCount && totalItemCount != 1 && !loading && loadingEnabled && isScrollable) {
                    setLoading(true)
                    consumer(listAdapter?.lastItemId ?: -1)
                }
            }

            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {}
        })
    }

    fun scrollUp(enabled: Boolean) {
        if (enabled) {
            smoothScrollBy(0, 0)
            setSelection(listAdapter!!.count - 4)
        }
    }

    private fun setLoading(loading: Boolean) {
        this.loading = loading
        progressBar!!.visibility = if (loading) View.VISIBLE else View.GONE
    }

    fun setLoadingEnabled(loadingEnabled: Boolean) {
        setLoading(false)
        this.loadingEnabled = loadingEnabled
    }

    fun loadIfNotScrollable(runnable: Runnable) {
        post {
            if (!isScrollable && !loading && loadingEnabled) {
                setLoading(true)
                runnable.run()
            }
        }
    }

    val isScrollable: Boolean
        get() {
            val lastVisibleItem = getChildAt(childCount - 1) as View
            return lastVisibleItem.bottom + paddingBottom >= (parent as View).height
        }

    private fun inflateProgressBar(): ProgressBar {
        val footerView = LayoutInflater.from(context).inflate(R.layout.progress_footer, null)
        addFooterView(footerView, null, false)
        return footerView.findViewById(R.id.progressBar)
    }
}