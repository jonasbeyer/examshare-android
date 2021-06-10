package de.twisted.imagepicker.imageeditengine.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import de.twisted.imagepicker.R;
import de.twisted.imagepicker.imageeditengine.PhotoEditorFragment;

public class PhotoEditorView extends FrameLayout {

    private CustomPaintView customPaintView;

    public PhotoEditorView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public PhotoEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public PhotoEditorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(Context context, AttributeSet attrs, int defStyle) {
        View view = inflate(context, R.layout.photo_editor_view, null);
        customPaintView = view.findViewById(R.id.paint_view);
        addView(view);
    }

    public void showPaintView() {
        customPaintView.setEnabled(true);
    }

    public void setBounds(RectF bitmapRect) {
        customPaintView.setBounds(bitmapRect);
    }

    public void setColor(int selectedColor) {
        customPaintView.setColor(selectedColor);
    }

    public int getColor() {
        return customPaintView.getColor();
    }

    public Bitmap getPaintBit() {
        return customPaintView.getPaintBit();
    }

    public void hidePaintView() {
        customPaintView.setEnabled(false);
    }

    public void setPhotoEditorFragment(PhotoEditorFragment photoEditorFragment) {
        customPaintView.setPhotoEditorFragment(photoEditorFragment);
    }

    public void reset() {
        customPaintView.reset();
        invalidate();
    }
}