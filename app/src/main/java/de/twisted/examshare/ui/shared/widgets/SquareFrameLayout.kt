package de.twisted.examshare.ui.shared.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/** A FrameLayout that will always be square -- same width and height,
 * where the height is based off the width.  */
class SquareFrameLayout : FrameLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Set getBitmap square layout.
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}