package de.twisted.imagepicker.imageeditengine;

import android.app.Activity;
import android.app.ActivityManager.TaskDescription;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager.LayoutParams;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import de.twisted.imagepicker.R;
import de.twisted.imagepicker.imageeditengine.utils.FragmentUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static de.twisted.imagepicker.imageeditengine.ImageEditor.EXTRA_IMAGE_PATH;

public class ImageEditActivity extends BaseImageEditActivity implements PhotoEditorFragment.OnFragmentInteractionListener, CropFragment.OnFragmentInteractionListener {

    private int position;
    private boolean imageView;
    private ArrayList<String> imagePaths;
    private Map<String, String> newImagePaths;

    //private View touchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_image_edit);
        getWindow().addFlags(LayoutParams.FLAG_FULLSCREEN);

        this.setTaskDescription();
        this.imagePaths = getIntent().getStringArrayListExtra(EXTRA_IMAGE_PATH);
        this.newImagePaths = new LinkedHashMap<>();
        if (imagePaths != null)
            showImage();
    }

    @Override
    public void onCropClicked(Bitmap bitmap, PhotoEditorFragment fragment) {
        this.imageView = false;
        FragmentUtil.addFragment(this, R.id.fragment_container, CropFragment.newInstance(bitmap, fragment));
    }

    @Override
    public void onDoneClicked(int number, String path) {
        if (path != null)
            newImagePaths.put(imagePaths.get(number - 1), path);
        if (number == imagePaths.size()) {
            Intent intent = new Intent();
            intent.putExtra(ImageEditor.EXTRA_EDITED_PATH, new Gson().toJson(newImagePaths));
            setResult(Activity.RESULT_OK, intent);
            finish();
            return;
        }

        position++;
        showImage();
    }

    @Override
    public void onImageCropped(Bitmap bitmap, Rect cropRect, int rotatedDegrees) {
        PhotoEditorFragment photoEditorFragment = (PhotoEditorFragment) FragmentUtil.getFragmentByTag(this,
                PhotoEditorFragment.class.getSimpleName() + position);
        if (photoEditorFragment != null) {
            photoEditorFragment.reset();
            photoEditorFragment.setImageBitmap(bitmap);
            photoEditorFragment.setCropRect(cropRect);
            photoEditorFragment.setRotatedDegrees(rotatedDegrees);
            FragmentUtil.removeFragment(this, (BaseFragment) FragmentUtil.getFragmentByTag(this, CropFragment.class.getSimpleName()));
            this.imageView = true;
        }
    }

    private void showImage() {
        if (position >= imagePaths.size())
            return;

        PhotoEditorFragment fragment = PhotoEditorFragment.newInstance(imagePaths.get(position), position + 1);
        fragment.setFragmentId(position);
        this.imageView = true;
        FragmentUtil.addFragment(this, R.id.fragment_container, fragment);
    }

    @Override
    public void onCancelCrop() {
        FragmentUtil.removeFragment(this, (BaseFragment) FragmentUtil.getFragmentByTag(this, CropFragment.class.getSimpleName()));
        this.imageView = true;
    }

    @Override
    public void onBackPressed() {
        if (!imageView) {
            onCancelCrop();
            return;
        }
        if (position == 0) {
            finish();
            return;
        }

        FragmentUtil.removeFragment(this, (BaseFragment) FragmentUtil.getFragmentByTag(this, PhotoEditorFragment.class.getSimpleName() + position));
        position--;
    }

    private void setTaskDescription() {
        if (Build.VERSION.SDK_INT < 21)
            return;

        int resId = getResources().getIdentifier("logo_without_text", "drawable", getPackageName());
        Bitmap bm = BitmapFactory.decodeResource(getResources(), resId);
        TaskDescription taskDesc = new TaskDescription(getString(R.string.app_name), bm, getResources().getColor(R.color.tabColor));
        setTaskDescription(taskDesc);
    }
}
