package de.twisted.examshare.ui.shared.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import kotlin.math.abs
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class CustomSwipeRefreshLayout : SwipeRefreshLayout {
    private var startGestureX: Float = 0f
    private var startGestureY: Float = 0f

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startGestureX = event.x
                startGestureY = event.y
            }

            MotionEvent.ACTION_MOVE -> {
                if (abs(event.x - startGestureX) > abs(event.y - startGestureY)) {
                    return false
                }
            }
        }

        return super.onInterceptTouchEvent(event)
    }
}