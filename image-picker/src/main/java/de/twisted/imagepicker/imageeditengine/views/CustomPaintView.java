package de.twisted.imagepicker.imageeditengine.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;

import de.twisted.imagepicker.imageeditengine.PhotoEditorFragment;

public class CustomPaintView extends View {

    private Paint mPaint;
    private Bitmap mDrawBit;
    private Paint mEraserPaint;
    private PhotoEditorFragment photoEditorFragment;

    private Canvas mPaintCanvas = null;

    private float last_x;
    private float last_y;
    private boolean eraser;
    private boolean enabled;

    private int mColor;
    private RectF bounds;

    public CustomPaintView(Context context) {
        super(context);
        init(context);
    }

    public CustomPaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        init(context);
    }

    public CustomPaintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomPaintView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mDrawBit == null) {
            generatorBit();
        }
    }

    private void generatorBit() {
        mDrawBit = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Config.ARGB_8888);
        mPaintCanvas = new Canvas(mDrawBit);
    }

    private void init(Context context) {
        mColor = Color.WHITE;
        bounds = new RectF(0, 0,getMeasuredWidth(), getMeasuredHeight());

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setFilterBitmap(true);
        mPaint.setStrokeWidth(15f);

        mEraserPaint = new Paint();
        mEraserPaint.setAlpha(0);
        mEraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mEraserPaint.setAntiAlias(true);
        mEraserPaint.setDither(true);
        mEraserPaint.setStyle(Paint.Style.STROKE);
        mEraserPaint.setStrokeJoin(Paint.Join.ROUND);
        mEraserPaint.setStrokeCap(Paint.Cap.ROUND);
        mEraserPaint.setStrokeWidth(40);
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        this.mColor = color;
        this.mPaint.setColor(mColor);
    }

    public void setWidth(float width) {
        this.mPaint.setStrokeWidth(width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDrawBit != null) {
            canvas.drawBitmap(mDrawBit, 0, 0, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!enabled)
            return super.onTouchEvent(event);

        boolean ret = super.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ret = true;
                last_x = x;
                last_y = y;

                break;
            case MotionEvent.ACTION_MOVE:
                ret = true;
                if (bounds.contains(x, y) && bounds.contains(last_x, last_y)) {
                    mPaintCanvas.drawLine(last_x, last_y, x, y, eraser ? mEraserPaint : mPaint);

                    if (photoEditorFragment != null)
                        photoEditorFragment.showResetButton();
                }
                last_x = x;
                last_y = y;

                this.invalidate();

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                ret = false;
                break;
        }
        return ret;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mDrawBit != null && !mDrawBit.isRecycled()) {
            mDrawBit.recycle();
        }
    }

    public void setEraser(boolean eraser) {
        this.eraser = eraser;
        mPaint.setColor(eraser ? Color.TRANSPARENT : mColor);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Bitmap getPaintBit() {
        return mDrawBit;
    }

    public void setPhotoEditorFragment(PhotoEditorFragment photoEditorFragment) {
        this.photoEditorFragment = photoEditorFragment;
    }

    public void reset() {
        if (mDrawBit != null && !mDrawBit.isRecycled()) {
            mDrawBit.recycle();
        }

        postInvalidate();
        generatorBit();
    }

    public void setBounds(RectF bitmapRect) {
        this.bounds = bitmapRect;
    }
}
