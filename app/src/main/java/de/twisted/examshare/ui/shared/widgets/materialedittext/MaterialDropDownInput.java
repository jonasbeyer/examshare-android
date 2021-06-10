package de.twisted.examshare.util.views.materialedittext;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

public class MaterialDropDownInput extends MaterialAutoCompleteTextView {

    private boolean isPopupShown;

    public MaterialDropDownInput(Context context) {
        super(context);
    }

    public MaterialDropDownInput(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaterialDropDownInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            performFiltering("", 0);
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
            setKeyListener(null);
            dismissDropDown();
        } else {
            this.isPopupShown = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return result;

        if (isPopupShown || super.isClearButtonTouched()) {
            dismissDropDown();
        } else {
            requestFocus();
            showDropDown();
        }

        super.setClearButtonTouched(false);
        return result;
    }

    @Override
    public void showDropDown() {
        super.showDropDown();
        this.isPopupShown = true;
    }

    @Override
    public void dismissDropDown() {
        super.dismissDropDown();
        this.isPopupShown = false;
    }
}