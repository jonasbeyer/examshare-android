package de.twisted.imagepicker.imageeditengine;

import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;

public class ImageEditor {

    public static final String EXTRA_IMAGE_PATH = "EXTRA_IMAGE_PATH";
    public static final String EXTRA_ORIGINAL = "EXTRA_ORIGINAL";
    public static final String EXTRA_CROP_RECT = "EXTRA_CROP_RECT";
    public static final String EXTRA_ROTATED_DEGREES = "EXTRA_ROTATED_DEGREES";

    public static final String EXTRA_EDITED_PATH = "EXTRA_EDITED_PATH";
    public static final String EXTRA_EDITED_NUMBER = "EXTRA_EDITED_NUMBER";

    public static final int RC_IMAGE_EDITOR = 0x34;

    public static class Builder {

        private final Activity context;
        private final ArrayList<String> imagePaths;

        public Builder(Activity context, ArrayList<String> imagePaths) {
            this.context = context;
            this.imagePaths = imagePaths;
        }

        public void open() {
            Intent intent = new Intent(context, ImageEditActivity.class);
            intent.putStringArrayListExtra(ImageEditor.EXTRA_IMAGE_PATH, imagePaths);
            context.startActivityForResult(intent, RC_IMAGE_EDITOR);
        }
    }
}
