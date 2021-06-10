package de.twisted.examshare.ui.shared.widgets

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import de.twisted.examshare.R

class VerticalSpaceItemDecoration(
    context: Context,
    @DimenRes dividerHeight: Int = R.dimen.list_item_vertical_spacing
) : RecyclerView.ItemDecoration() {

    private val dividerHeight = context.resources.getDimension(dividerHeight).toInt()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) != 0)
            outRect.top = dividerHeight
    }
}