package de.twisted.imagepicker.imageeditengine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import de.twisted.imagepicker.R;
import de.twisted.imagepicker.imageeditengine.utils.Matrix3;
import de.twisted.imagepicker.imageeditengine.utils.ProcessingImage;
import de.twisted.imagepicker.imageeditengine.utils.Utility;
import de.twisted.imagepicker.imageeditengine.views.PhotoEditorView;
import de.twisted.imagepicker.imageeditengine.views.VerticalSlideColorPicker;
import de.twisted.imagepicker.imageeditengine.views.imagezoom.ImageViewTouch;

public class PhotoEditorFragment extends BaseFragment implements View.OnClickListener {

    ImageViewTouch mainImageView;
    ImageView cropButton;
    PhotoEditorView photoEditorView;
    ImageView paintButton;
    ImageView resetButton;
    ImageView backIv;
    VerticalSlideColorPicker colorPickerView;
    View toolbarLayout;
    FrameLayout doneBtn;
    ProgressBar progressBar;
    TextView imageCount;
    private String imagePath;
    private int imageNumber;
    private Bitmap mainBitmap;
    private LruCache<Integer, Bitmap> cacheStack;
    private OnFragmentInteractionListener mListener;
    public static final int MODE_NONE = 0;
    public static final int MODE_PAINT = 1;

    private Rect cropRect;
    private int rotatedDegrees;

    protected int currentMode;
    private Bitmap originalBitmap;

    public static PhotoEditorFragment newInstance(String imagePath, int imageNumber) {
        Bundle bundle = new Bundle();
        // bundle.putParcelable("bitmap", bitmap);
        bundle.putString(ImageEditor.EXTRA_IMAGE_PATH, imagePath);
        bundle.putInt(ImageEditor.EXTRA_EDITED_NUMBER, imageNumber);
        PhotoEditorFragment photoEditorFragment = new PhotoEditorFragment();
        photoEditorFragment.setArguments(bundle);
        return photoEditorFragment;
    }

    public PhotoEditorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_editor, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(
                    context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setCropRect(Rect cropRect) {
        this.cropRect = cropRect;
    }

    public Rect getCropRect() {
        return cropRect;
    }

    public void setRotatedDegrees(int rotatedDegrees) {
        this.rotatedDegrees = rotatedDegrees;
    }

    public int getRotatedDegrees() {
        return rotatedDegrees;
    }

    public interface OnFragmentInteractionListener {
        void onCropClicked(Bitmap bitmap, PhotoEditorFragment fragment);

        void onDoneClicked(int number, String path);
    }

    public void setImageBitmap(Bitmap bitmap) {
        mainBitmap = bitmap;
        mainImageView.setImageBitmap(bitmap);
        mainImageView.post(() -> photoEditorView.setBounds(mainImageView.getBitmapRect()));
    }

    // public void setImageWithRect(Rect rect) {
    //     mainBitmap = getCroppedBitmap(originalBitmap, rect);
    //     mainImageView.setImageBitmap(mainBitmap);
    //     mainImageView.post(() -> photoEditorView.setBounds(mainImageView.getBitmapRect()));
    // }
    //
    // private Bitmap getCroppedBitmap(Bitmap srcBitmap, Rect rect) {
    //     return Bitmap.createBitmap(srcBitmap,
    //             rect.left,
    //             rect.top,
    //             (rect.right - rect.left),
    //             (rect.bottom - rect.top));
    // }

    public void reset() {
        photoEditorView.reset();
        resetButton.setVisibility(View.GONE);
    }

    protected void initView(View view) {
        mainImageView = view.findViewById(R.id.image_iv);
        cropButton = view.findViewById(R.id.crop_btn);
        resetButton = view.findViewById(R.id.reset_view);
        photoEditorView = view.findViewById(R.id.photo_editor_view);
        paintButton = view.findViewById(R.id.paint_btn);
        colorPickerView = view.findViewById(R.id.color_picker_view);
        backIv = view.findViewById(R.id.back_iv);
        toolbarLayout = view.findViewById(R.id.toolbar_layout);
        doneBtn = view.findViewById(R.id.done_btn);
        imageCount = view.findViewById(R.id.img_count);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(android.R.color.white), Mode.SRC_IN);

        this.imagePath = getArguments().getString(ImageEditor.EXTRA_IMAGE_PATH);
        this.imageNumber = getArguments().getInt(ImageEditor.EXTRA_EDITED_NUMBER);
        this.imageCount.setText(String.valueOf(imageNumber));
        this.loadImage();

        photoEditorView.setPhotoEditorFragment(this);
        cropButton.setOnClickListener(this);
        paintButton.setOnClickListener(this);
        doneBtn.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        backIv.setOnClickListener(this);

        colorPickerView.setOnColorChangeListener(selectedColor -> {
            photoEditorView.setColor(selectedColor);
            if (currentMode == MODE_PAINT)
                paintButton.setBackground(Utility.tintDrawable(getContext(), R.drawable.circle, selectedColor));
        });
    }

    private void loadImage() {
        Glide.with(this)
                .asBitmap()
                .apply(new RequestOptions().centerInside())
                .load(imagePath).into(new SimpleTarget<Bitmap>(1600, 1600) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                originalBitmap = resource;
                cropButton.setVisibility(View.VISIBLE);
                paintButton.setVisibility(View.VISIBLE);
                doneBtn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                setImageBitmap(originalBitmap);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                mListener.onDoneClicked(imageNumber, null);
            }
        });
    }

    protected void onModeChanged(int currentMode) {
        onPaintMode(currentMode == MODE_PAINT);
        if (currentMode == MODE_PAINT) {
            AnimationHelper.animate(getContext(), colorPickerView, R.anim.item_slide_in_right, View.VISIBLE, null);
        } else {
            AnimationHelper.animate(getContext(), colorPickerView, R.anim.item_slide_out_right, View.INVISIBLE, null);
        }
    }

    @Override
    public void onClick(final View view) {
        int id = view.getId();
        if (id == R.id.reset_view) {
            this.reset();
        } else if (id == R.id.crop_btn) {
            mListener.onCropClicked(originalBitmap, this);
        } else if (id == R.id.paint_btn) {
            setMode(MODE_PAINT);
        } else if (id == R.id.back_iv) {
            getActivity().onBackPressed();
        } else if (id == R.id.done_btn) {
            new ProcessingImage(getBitmapCache(mainBitmap), Utility.getCacheFilePath(view.getContext()), data -> mListener.onDoneClicked(imageNumber, data)).execute();
        }

        if (currentMode != MODE_NONE) {
            mainImageView.animate().scaleX(1f);
            photoEditorView.animate().scaleX(1f);
            mainImageView.animate().scaleY(1f);
            photoEditorView.animate().scaleY(1f);
            // touchView.setVisibility(View.GONE);
        }
    }

    private void onPaintMode(boolean status) {
        if (status) {
            paintButton.setBackground(Utility.tintDrawable(getContext(), R.drawable.circle, photoEditorView.getColor()));
            photoEditorView.showPaintView();
        } else {
            paintButton.setBackground(null);
            photoEditorView.hidePaintView();
        }
    }

    public void showResetButton() {
        resetButton.setVisibility(View.VISIBLE);
    }

    private Bitmap getBitmapCache(Bitmap bitmap) {
        Matrix touchMatrix = mainImageView.getImageViewMatrix();

        Bitmap resultBit = Bitmap.createBitmap(bitmap).copy(Config.ARGB_8888, true);
        Canvas canvas = new Canvas(resultBit);

        float[] data = new float[9];
        touchMatrix.getValues(data);
        Matrix3 cal = new Matrix3(data);
        Matrix3 inverseMatrix = cal.inverseMatrix();
        Matrix m = new Matrix();
        m.setValues(inverseMatrix.getValues());

        float[] f = new float[9];
        m.getValues(f);
        int dx = (int) f[Matrix.MTRANS_X];
        int dy = (int) f[Matrix.MTRANS_Y];
        float scale_x = f[Matrix.MSCALE_X];
        float scale_y = f[Matrix.MSCALE_Y];
        canvas.save();
        canvas.translate(dx, dy);
        canvas.scale(scale_x, scale_y);

        if (photoEditorView.getPaintBit() != null)
            canvas.drawBitmap(photoEditorView.getPaintBit(), 0, 0, null);

        canvas.restore();
        return resultBit;
    }

    protected void setMode(int mode) {
        if (currentMode != mode) {
            onModeChanged(mode);
        } else {
            mode = MODE_NONE;
            onModeChanged(mode);
        }
        this.currentMode = mode;
    }
}
