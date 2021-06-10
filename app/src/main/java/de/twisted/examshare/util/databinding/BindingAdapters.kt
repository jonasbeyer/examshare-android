package de.twisted.examshare.util.databinding

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.getDrawableOrThrow
import androidx.core.content.res.use
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.twisted.examshare.R

@BindingAdapter("visible")
fun setVisible(view: View, visible: Boolean?) {
    view.isVisible = visible ?: false
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("items")
fun <T> setItemList(view: RecyclerView, items: List<T>?) {
    val adapter = view.adapter as? ListAdapter<T, *> ?: return
    if (items != null) {
        adapter.submitList(items)
    }
}

@BindingAdapter("commentBackground")
fun setCommentBackground(view: View, isParent: Boolean) {
    if (isParent) {
       view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.light_grey))
    } else {
        view.background = view.context.obtainStyledAttributes(
            intArrayOf(android.R.attr.selectableItemBackground)
        ).use {
            it.getDrawableOrThrow(0)
        }
    }
}