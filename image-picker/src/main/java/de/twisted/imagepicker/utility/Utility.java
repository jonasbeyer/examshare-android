package de.twisted.imagepicker.utility;

import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.twisted.imagepicker.R;
import de.twisted.imagepicker.modals.Img;

public class Utility {

    public static int HEIGHT, WIDTH;
    private String pathDir;

    public static void setupStatusBarHidden(AppCompatActivity appCompatActivity) {
        Window w = appCompatActivity.getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public static void showStatusBar(AppCompatActivity appCompatActivity) {
        synchronized (appCompatActivity) {
            Window w = appCompatActivity.getWindow();
            View decorView = w.getDecorView();
            // Show Status Bar.
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);

        }
    }

    public static void hideStatusBar(AppCompatActivity appCompatActivity) {
        synchronized (appCompatActivity) {
            Window w = appCompatActivity.getWindow();
            View decorView = w.getDecorView();
            // Hide Status Bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public static void getScreenSize(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        HEIGHT = displayMetrics.heightPixels;
        WIDTH = displayMetrics.widthPixels;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static String getDateDifference(Context context, Calendar calendar) {
        Date d = calendar.getTime();
        Calendar lastMonth = Calendar.getInstance();
        Calendar lastWeek = Calendar.getInstance();
        Calendar recent = Calendar.getInstance();
        lastMonth.add(Calendar.DAY_OF_MONTH, -(Calendar.DAY_OF_MONTH));
        lastWeek.add(Calendar.DAY_OF_MONTH, -7);
        recent.add(Calendar.DAY_OF_MONTH, -2);
        if (calendar.before(lastMonth)) {
            return new SimpleDateFormat("MMMM", Locale.getDefault()).format(d);
        } else if (calendar.after(lastMonth) && calendar.before(lastWeek)) {
            return context.getResources().getString(R.string.last_month);
        } else if (calendar.after(lastWeek) && calendar.before(recent)) {
            return context.getResources().getString(R.string.last_week);
        } else {
            return context.getResources().getString(R.string.recent);
        }
    }

    public static boolean isNull(View topChild) {
        return topChild == null;
    }

    public static Cursor getCursor(Context context) {
        return context.getContentResolver().query(Constants.URI, Constants.PROJECTION,
                null, null, Constants.ORDERBY);
    }

    public static boolean isViewVisible(View view) {
        return view != null && view.getVisibility() == View.VISIBLE;
    }

    public static ViewPropertyAnimator showScrollbar(View mScrollbar, Context context) {
        mScrollbar.setVisibility(View.VISIBLE);
        return mScrollbar.animate().translationX(0f).alpha(1f)
                .setDuration(Constants.sScrollbarAnimDuration)
                .setListener(new AnimatorListenerAdapter() {
                    // adapter required for new alpha value to stick
                });
    }

    public static void cancelAnimation(ViewPropertyAnimator animator) {
        if (animator != null) {
            animator.cancel();
        }
    }

    public static void manipulateVisibility(AppCompatActivity activity, float slideOffset,
                                            RecyclerView instantRecyclerView, RecyclerView recyclerView,
                                            View status_bar_bg, AppBarLayout appBar, View clickme, View sendButton, List<Img> imgs) {
        instantRecyclerView.setAlpha(1 - slideOffset);
        clickme.setAlpha(1 - slideOffset);
        sendButton.setAlpha(1 - slideOffset);
        appBar.setAlpha(slideOffset);
        recyclerView.setAlpha(slideOffset);
        if ((1 - slideOffset) == 0 && instantRecyclerView.getVisibility() == View.VISIBLE) {
            instantRecyclerView.setVisibility(View.GONE);
            clickme.setVisibility(View.GONE);
        } else if (instantRecyclerView.getVisibility() == View.GONE && (1 - slideOffset) > 0) {
            instantRecyclerView.setVisibility(View.VISIBLE);
            clickme.setVisibility(View.VISIBLE);
            sendButton.clearAnimation();
            if (!imgs.isEmpty())
                sendButton.setVisibility(View.VISIBLE);
        }
        if ((slideOffset) > 0 && recyclerView.getVisibility() == View.INVISIBLE) {
            recyclerView.setVisibility(View.VISIBLE);
            status_bar_bg.animate().translationY(0).setDuration(300).start();
            appBar.setVisibility(View.VISIBLE);
            Utility.showStatusBar(activity);
        } else if (recyclerView.getVisibility() == View.VISIBLE && (slideOffset) == 0) {
            Utility.hideStatusBar(activity);
            recyclerView.setVisibility(View.INVISIBLE);
            appBar.setVisibility(View.GONE);
            status_bar_bg.animate().translationY(-(status_bar_bg.getHeight())).setDuration(300).start();
        }
    }

    @SuppressWarnings("SameParameterValue")
    public static int getValueInRange(int min, int max, int value) {
        int minimum = Math.max(min, value);
        return Math.min(minimum, max);
    }

    public static File getFile() {
        File dir = new File(Environment.getExternalStorageDirectory(), "/Pictures/ExamShare");
        if (!dir.exists())
            dir.mkdir();
        File photo = new File(dir, "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmSS", Locale.ENGLISH).format(new Date()) + ".jpg");
        if (photo.exists()) {
            photo.delete();
        }

        return photo;
    }

    public static void refreshImage(Context context, File file, Runnable refreshed) {
        ExMediaScannerConnectionClient client = new ExMediaScannerConnectionClient();
        MediaScannerConnection scanner = new MediaScannerConnection(context, client);
        client.setScanner(scanner, file, refreshed);
        scanner.connect();
    }

    public static Bitmap getExifCorrectedBitmap(File f) {
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(f.getAbsolutePath(), bounds);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(f.getAbsolutePath(), opts);
        try {
            ExifInterface exif = new ExifInterface(f.getAbsolutePath());

            String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;

            int rotationAngle = 0;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

            Matrix matrix = new Matrix();
            matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
            return Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getScaledBitmap(int maxWidth, Bitmap rotatedBitmap) {
        int nh = (int) (rotatedBitmap.getHeight() * (512.0 / rotatedBitmap.getWidth()));
        return Bitmap.createScaledBitmap(rotatedBitmap, maxWidth, nh, true);
    }

    public static Bitmap rotate(Bitmap bitmap, int i) {
        Matrix matrix = new Matrix();
        matrix.postRotate(i);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        return Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
    }

    public List<Uri> getImagesFromGallary(Context context) {
        List<Uri> images = new ArrayList<>();
        Cursor imageCursor = null;
        try {
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.ORIENTATION};
            final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
            imageCursor = context.getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
            while (imageCursor.moveToNext()) {
                Uri uri = Uri.parse(imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                images.add(uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (imageCursor != null && !imageCursor.isClosed()) {
                imageCursor.close();
            }
        }
        return images;
    }

    @NonNull
    private Uri[] getAllMediaThumbnailsPath(Context context, long id,
                                            Boolean exceptGif) {
        ContentResolver resolver = context.getContentResolver();
        String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        String bucketId = String.valueOf(id);
        String sort = MediaStore.Images.Media._ID + " DESC";
        String[] selectionArgs = {bucketId};

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor c;
        if (!bucketId.equals("0")) {
            c = resolver.query(images, null, selection, selectionArgs, sort);
        } else {
            c = resolver.query(images, null, null, null, sort);
        }
        Uri[] imageUris = new Uri[c == null ? 0 : c.getCount()];
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    setPathDir(c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA)),
                            c.getString(c.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
                    int position = -1;
                    RegexUtil regexUtil = new RegexUtil();
                    do {
                        if (exceptGif &&
                                regexUtil.checkGif(c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA))))
                            continue;
                        int imgId = c.getInt(c.getColumnIndex(MediaStore.MediaColumns._ID));
                        Uri path = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + imgId);
                        imageUris[++position] = path;
                    } while (c.moveToNext());
                }
                c.close();
            } catch (Exception e) {
                if (!c.isClosed()) c.close();
            }
        }
        return imageUris;
    }

    private String setPathDir(String path, String fileName) {
        return pathDir = path.replace("/" + fileName, "");
    }

    public String getPathDir(Long bucketId) {
        if (pathDir.equals("") || bucketId == 0)
            pathDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM + "/Camera").getAbsolutePath();
        return pathDir;
    }
}
