package de.twisted.imagepicker.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager.TaskDescription;
import android.content.Intent;
import android.database.Cursor;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import de.twisted.imagepicker.R;
import de.twisted.imagepicker.adapters.InstantImageAdapter;
import de.twisted.imagepicker.adapters.MainImageAdapter;
import de.twisted.imagepicker.imageeditengine.ImageEditor;
import de.twisted.imagepicker.interfaces.OnSelectionListener;
import de.twisted.imagepicker.modals.Img;
import de.twisted.imagepicker.utility.Constants;
import de.twisted.imagepicker.utility.HeaderItemDecoration;
import de.twisted.imagepicker.utility.ImageFetcher;
import de.twisted.imagepicker.utility.PermUtil;
import de.twisted.imagepicker.utility.Utility;
import io.fotoapparat.Fotoapparat;
import io.fotoapparat.configuration.CameraConfiguration;
import io.fotoapparat.parameter.ScaleType;
import io.fotoapparat.selector.AspectRatioSelectorsKt;
import io.fotoapparat.selector.FlashSelectorsKt;
import io.fotoapparat.selector.FocusModeSelectorsKt;
import io.fotoapparat.selector.LensPositionSelectorsKt;
import io.fotoapparat.selector.ResolutionSelectorsKt;
import io.fotoapparat.selector.SelectorsKt;
import io.fotoapparat.view.CameraView;
import io.fotoapparat.view.FocusView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class ImagePicker extends AppCompatActivity implements View.OnTouchListener {

    private static final int sBubbleAnimDuration = 1000;
    private static final int sScrollbarHideDelay = 1000;
    private static final String SELECTION = "selection";
    private static final String IMAGES = "images";
    private static final int sTrackSnapRange = 5;
    public static String IMAGE_RESULTS = "image_results";
    int colorPrimaryDark;

    Fotoapparat fotoapparat;
    private Handler handler = new Handler();
    private CameraView mCamera;
    private List<String> images;
    private boolean processing;
    private RecyclerView recyclerView, instantRecyclerView;

    private BottomSheetBehavior mBottomSheetBehavior;
    private InstantImageAdapter initaliseadapter;
    private GridLayoutManager mLayoutManager;
    private AppBarLayout appBarLayout;
    private FocusView focusView;
    private View status_bar_bg, mScrollbar, bottomButtons, sendButton;
    private TextView mBubbleView, img_count;
    private ImageView mHandleView, clickme;
    private ViewPropertyAnimator mScrollbarAnimator;
    private ViewPropertyAnimator mBubbleAnimator;
    private List<Img> selectionList = new ArrayList<>();
    private List<String> takenPhotos = new ArrayList<>();
    private Runnable mScrollbarHider = () -> hideScrollbar();
    private MainImageAdapter mainImageAdapter;
    private float mViewHeight;
    private boolean mHideScrollbar = true;
    private int SelectionCount = 1;
    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!mHandleView.isSelected() && recyclerView.isEnabled()) {
                setViewPositions(getScrollProportion(recyclerView));
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (recyclerView.isEnabled()) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        handler.removeCallbacks(mScrollbarHider);
                        Utility.cancelAnimation(mScrollbarAnimator);
                        if (!Utility.isViewVisible(mScrollbar) && (recyclerView.computeVerticalScrollRange() - mViewHeight > 0)) {
                            mScrollbarAnimator = Utility.showScrollbar(mScrollbar, ImagePicker.this);
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        if (mHideScrollbar && !mHandleView.isSelected()) {
                            handler.postDelayed(mScrollbarHider, sScrollbarHideDelay);
                        }
                        break;
                }
            }
        }
    };

    private void hideSendButton() {
        sendButton.setVisibility(View.GONE);
        Animation anim = new ScaleAnimation(
                1f, 0f, // Start and end values for the X axis scaling
                1f, 0f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(300);
        sendButton.startAnimation(anim);
    }

    private OnSelectionListener onSelectionListener = new OnSelectionListener() {
        @Override
        public void OnClick(Img img, View view, int position) {
            if (selectionList.size() == 0) {
                if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sendButton.setVisibility(View.VISIBLE);
                    Animation anim = new ScaleAnimation(
                            0f, 1f, // Start and end values for the X axis scaling
                            0f, 1f, // Start and end values for the Y axis scaling
                            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                            Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                    anim.setFillAfter(true); // Needed to keep the result of the animation
                    anim.setDuration(300);
                    sendButton.startAnimation(anim);
                }
            }
            if (selectionList.contains(img)) {
                selectionList.remove(img);
                initaliseadapter.select(false, position);
                mainImageAdapter.select(false, position);

                if (selectionList.isEmpty()) {
                    getSupportActionBar().setTitle(getResources().getQuantityString(R.plurals.select_image, SelectionCount));
                    hideSendButton();
                } else {
                    getSupportActionBar().setTitle(getResources().getQuantityString(R.plurals.selected, selectionList.size(), selectionList.size()));
                }
            } else {
                if (SelectionCount <= selectionList.size())
                    return;

                img.setPosition(position);
                selectionList.add(img);
                initaliseadapter.select(true, position);
                mainImageAdapter.select(true, position);
                getSupportActionBar().setTitle(getResources().getQuantityString(R.plurals.selected, selectionList.size(), selectionList.size()));
            }

            img_count.setText(String.valueOf(selectionList.size()));
        }
    };
    private FrameLayout flash;
    private ImageView front;
    private boolean isback = true;
    private int flashDrawable;

    public static void start(final Activity context, final ArrayList<String> images, final int requestCode, final int selectionCount) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermUtil.checkForPermissions(context, true, check -> {
                Intent i = new Intent(context, ImagePicker.class);
                i.putExtra(SELECTION, selectionCount);
                i.putStringArrayListExtra(IMAGES, images);
                context.startActivityForResult(i, requestCode);
            });
        } else {
            Intent i = new Intent(context, ImagePicker.class);
            i.putExtra(SELECTION, selectionCount);
            i.putStringArrayListExtra(IMAGES, images);
            context.startActivityForResult(i, requestCode);
        }
    }

    public static void start(final FragmentActivity context, ArrayList<String> images, int requestCode) {
        start(context, images, requestCode, 1);
    }

    private void hideScrollbar() {
        float transX = getResources().getDimensionPixelSize(R.dimen.fastscroll_scrollbar_padding_end);
        mScrollbarAnimator = mScrollbar.animate().translationX(transX).alpha(0f)
                .setDuration(Constants.sScrollbarAnimDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mScrollbar.setVisibility(View.GONE);
                        mScrollbarAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        super.onAnimationCancel(animation);
                        mScrollbar.setVisibility(View.GONE);
                        mScrollbarAnimator = null;
                    }
                });
    }

    public void returnObjects(String editedImages) {
        removePhotos(true, () -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(IMAGE_RESULTS, editedImages);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setupStatusBarHidden(this);
        Utility.hideStatusBar(this);
        setContentView(R.layout.activity_main_lib);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // setTaskDescription();
        initialize();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fotoapparat.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fotoapparat.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        fotoapparat.stop();
    }

    private void initialize() {
        Utility.getScreenSize(this);
        try {
            SelectionCount = getIntent().getIntExtra(SELECTION, 1);
            images = getIntent().getStringArrayListExtra(IMAGES);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String quantityString = getResources().getQuantityString(R.plurals.selected, images.size(), images.size());
        getSupportActionBar().setTitle(images.isEmpty() ? getResources().getQuantityString(R.plurals.select_image, SelectionCount) : quantityString);
        colorPrimaryDark = ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, getTheme());
        mCamera = findViewById(R.id.camera_view);
        focusView = findViewById(R.id.focusView);

        initializeCamera();

        clickme = findViewById(R.id.clickme);
        flash = findViewById(R.id.flash);
        front = findViewById(R.id.front);
        appBarLayout = findViewById(R.id.appBarLayout);
        sendButton = findViewById(R.id.sendButton);
        img_count = findViewById(R.id.img_count);

        if (!images.isEmpty())
            img_count.setText(String.valueOf(images.size()));
        mBubbleView = findViewById(R.id.fastscroll_bubble);
        mHandleView = findViewById(R.id.fastscroll_handle);
        mScrollbar = findViewById(R.id.fastscroll_scrollbar);
        mScrollbar.setVisibility(View.GONE);
        mBubbleView.setVisibility(View.GONE);
        bottomButtons = findViewById(R.id.bottomButtons);

        status_bar_bg = findViewById(R.id.status_bar_bg);
        instantRecyclerView = findViewById(R.id.instantRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        instantRecyclerView.setLayoutManager(linearLayoutManager);
        initaliseadapter = new InstantImageAdapter(this);
        initaliseadapter.addOnSelectionListener(onSelectionListener);
        instantRecyclerView.setAdapter(initaliseadapter);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addOnScrollListener(mScrollListener);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) sendButton.getLayoutParams();
        layoutParams.setMargins(0, 0, (int) (Utility.convertDpToPixel(16, this)),
                (int) (Utility.convertDpToPixel(174, this)));
        sendButton.setLayoutParams(layoutParams);
        sendButton.setVisibility(images.isEmpty() ? View.GONE : View.VISIBLE);
        mainImageAdapter = new MainImageAdapter(this);
        mLayoutManager = new GridLayoutManager(this, MainImageAdapter.SPAN_COUNT);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mainImageAdapter.getItemViewType(position) == MainImageAdapter.HEADER) {
                    return MainImageAdapter.SPAN_COUNT;
                }
                return 1;
            }
        });
        recyclerView.setLayoutManager(mLayoutManager);
        mainImageAdapter.addOnSelectionListener(onSelectionListener);
        recyclerView.setAdapter(mainImageAdapter);
        recyclerView.addItemDecoration(new HeaderItemDecoration(this, recyclerView, mainImageAdapter));
        mHandleView.setOnTouchListener(this);
        clickme.setOnClickListener(view -> {
            final File file = Utility.getFile();
            this.processing = true;
            fotoapparat.takePicture().saveToFile(file).whenDone((unit) -> {
                fotoapparat.autoFocus();
                takenPhotos.add(file.getAbsolutePath());
                Utility.refreshImage(ImagePicker.this, file, () -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());

                    final String header = Utility.getDateDifference(ImagePicker.this, calendar);
                    final Img img = new Img(header, file.getAbsolutePath(), file.getAbsolutePath(), "");
                    img.setSelected(selectionList.size() < SelectionCount);

                    if (mainImageAdapter.getItemCount() > 0 && !mainImageAdapter.getItemList().get(0).getHeaderDate().equals(header))
                        mainImageAdapter.getItemList().add(0, new Img(header, "", "", ""));

                    mainImageAdapter.addImageFirst(img);
                    initaliseadapter.addImageFirst(img);

                    if (selectionList.size() < SelectionCount) {
                        selectionList.add(img);
                        sendButton.setVisibility(View.VISIBLE);
                        img_count.setText(String.valueOf(selectionList.size()));
                        getSupportActionBar().setTitle(getResources().getQuantityString(R.plurals.selected, selectionList.size(), selectionList.size()));
                    }
                    this.processing = false;
                });
            });
        });
        sendButton.setOnClickListener(view -> passToEditor());

        final ImageView iv = (ImageView) flash.getChildAt(0);
        flashDrawable = R.drawable.ic_flash_off_black;
        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int height = flash.getHeight();
                iv.animate().translationY(height).setDuration(100).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        iv.setTranslationY(-(height / 2));
                        if (flashDrawable == R.drawable.ic_flash_auto_black) {
                            flashDrawable = R.drawable.ic_flash_off_black;
                            iv.setImageResource(flashDrawable);
                            fotoapparat.updateConfiguration(CameraConfiguration.builder()
                                    .photoResolution((AspectRatioSelectorsKt.wideRatio(ResolutionSelectorsKt.highestResolution())))
                                    .flash(FlashSelectorsKt.off()).build());
                        } else if (flashDrawable == R.drawable.ic_flash_off_black) {
                            flashDrawable = R.drawable.ic_flash_on_black;
                            iv.setImageResource(flashDrawable);
                            fotoapparat.updateConfiguration(CameraConfiguration.builder()
                                    .photoResolution((AspectRatioSelectorsKt.wideRatio(ResolutionSelectorsKt.highestResolution())))
                                    .flash(FlashSelectorsKt.on()).build());
                        } else {
                            flashDrawable = R.drawable.ic_flash_auto_black;
                            iv.setImageResource(flashDrawable);
                            fotoapparat.updateConfiguration(CameraConfiguration.builder()
                                    .photoResolution((AspectRatioSelectorsKt.wideRatio(ResolutionSelectorsKt.highestResolution())))
                                    .flash(FlashSelectorsKt.autoRedEye()).build());
                        }
                        fotoapparat.focus();
                        iv.animate().translationY(0).setDuration(50).setListener(null).start();
                    }
                }).start();
            }
        });

        front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(front, "scaleX", 1f, 0f).setDuration(150);
                final ObjectAnimator oa2 = ObjectAnimator.ofFloat(front, "scaleX", 0f, 1f).setDuration(150);
                oa1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        front.setImageResource(R.drawable.ic_photo_camera);
                        oa2.start();
                    }
                });
                oa1.start();
                if (isback) {
                    isback = false;
                    fotoapparat.switchTo(LensPositionSelectorsKt.front(), new CameraConfiguration());
                } else {
                    isback = true;
                    fotoapparat.switchTo(LensPositionSelectorsKt.back(), CameraConfiguration.builder()
                            .photoResolution((AspectRatioSelectorsKt.wideRatio(ResolutionSelectorsKt.highestResolution())))
                            .build());
                }
            }
        });

        updateImages();
    }

    private void initializeCamera() {
        fotoapparat = Fotoapparat.with(this)
                .into(mCamera)
                .focusView(focusView)
                .previewScaleType(ScaleType.CenterInside)
                .photoResolution((AspectRatioSelectorsKt.wideRatio(ResolutionSelectorsKt.highestResolution())))
                .lensPosition(LensPositionSelectorsKt.back())
                .focusMode(SelectorsKt.firstAvailable(
                        FocusModeSelectorsKt.continuousFocusPicture(),
                        FocusModeSelectorsKt.autoFocus(),
                        FocusModeSelectorsKt.fixed()))
                .build();
        fotoapparat.start();
        fotoapparat.autoFocus();
    }

    // private void setTaskDescription() {
    //     int resId = getResources().getIdentifier("logo_without_text", "drawable", getPackageName());
    //     Bitmap bm = BitmapFactory.decodeResource(getResources(), resId);
    //
    //     TaskDescription taskDesc = new TaskDescription(getString(R.string.app_name), bm, getResources().getColor(R.color.tabColor));
    //     setTaskDescription(taskDesc);
    // }

    private void passToEditor() {
        if (selectionList.isEmpty()) {
            removePhotos(true, () -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(IMAGE_RESULTS, new Gson().toJson(Collections.emptyMap()));
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            });
            return;
        }

        ArrayList<String> pathList = new ArrayList<>();
        Iterator<Img> iterator = selectionList.iterator();
        while (iterator.hasNext()) {
            Img img = iterator.next();
            File file = new File(img.getUrl());
            if (!file.exists()) {
                iterator.remove();
                mainImageAdapter.removeImage(img.getUrl());
                initaliseadapter.removeImage(img.getUrl());
                continue;
            }
            pathList.add(img.getUrl());
        }

        String quantityString = getResources().getQuantityString(R.plurals.selected, selectionList.size(), selectionList.size());
        getSupportActionBar().setTitle(selectionList.isEmpty() ? getResources().getQuantityString(R.plurals.select_image, SelectionCount) : quantityString);

        if (!selectionList.isEmpty())
            img_count.setText(String.valueOf(selectionList.size()));
        if (mainImageAdapter.getItemList().isEmpty()) {
            hideScrollbar();
            hideBubble();
        }

        if (pathList.isEmpty()) {
            hideSendButton();
            Toast.makeText(this, R.string.images_dont_exist, Toast.LENGTH_SHORT).show();
            return;
        }

        new ImageEditor.Builder(this, pathList).open();
    }

    @SuppressLint("StaticFieldLeak")
    private void updateImages() {
        mainImageAdapter.clearList();
        Cursor cursor = Utility.getCursor(this);
        if (cursor == null) {
            setBottomSheetBehavior();
            return;
        }

        ArrayList<Img> INSTANTLIST = new ArrayList<>();
        String header = "";
        int limit = 100;
        if (cursor.getCount() < 100)
            limit = cursor.getCount();
        int date = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
        int data = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        int contentUrl = cursor.getColumnIndex(MediaStore.Images.Media._ID);
        Calendar calendar;
        for (int i = 0; i < limit; i++) {
            cursor.moveToNext();
            Uri path = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + cursor.getInt(contentUrl));
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(cursor.getLong(date));
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
            String dateDifference = Utility.getDateDifference(ImagePicker.this, calendar);

            if (!header.equalsIgnoreCase("" + dateDifference)) {
                header = "" + dateDifference;
                Img img = new Img("" + dateDifference, "", "", dateFormat.format(calendar.getTime()));
                INSTANTLIST.add(img);
            }
            Img img = new Img("" + header, "" + path, cursor.getString(data), dateFormat.format(calendar.getTime()));
            img.setSelected(images.contains(img.getUrl()));

            if (images.contains(img.getUrl()))
                selectionList.add(img);
            INSTANTLIST.add(img);
        }
        cursor.close();
        new ImageFetcher(ImagePicker.this) {
            @Override
            protected void onPostExecute(ArrayList<Img> imgs) {
                super.onPostExecute(imgs);
                mainImageAdapter.addImageList(imgs);
            }
        }.execute(Utility.getCursor(ImagePicker.this));
        initaliseadapter.addImageList(INSTANTLIST);
        mainImageAdapter.addImageList(INSTANTLIST);
        images.clear();
        setBottomSheetBehavior();
    }

    private void setBottomSheetBehavior() {
        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight((int) (Utility.convertDpToPixel(194, this)));
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {}

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Utility.manipulateVisibility(ImagePicker.this, slideOffset,
                        instantRecyclerView, recyclerView, status_bar_bg,
                        appBarLayout, bottomButtons, sendButton, selectionList);
                if (slideOffset == 1) {
                    if (mainImageAdapter.getItemCount() > 0)
                        Utility.showScrollbar(mScrollbar, ImagePicker.this);
                    sendButton.setVisibility(View.GONE);
                    mainImageAdapter.notifyDataSetChanged();
                    mViewHeight = mScrollbar.getMeasuredHeight();
                    handler.post(() -> setViewPositions(getScrollProportion(recyclerView)));
                } else if (slideOffset == 0) {
                    initaliseadapter.notifyDataSetChanged();
                    hideScrollbar();
                    img_count.setText(String.valueOf(selectionList.size()));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.selectionOk)
            passToEditor();
        if (item.getItemId() == android.R.id.home)
            removePhotos(false, () -> super.onBackPressed());

        return super.onOptionsItemSelected(item);
    }

    private float getScrollProportion(RecyclerView recyclerView) {
        final int verticalScrollOffset = recyclerView.computeVerticalScrollOffset();
        final int verticalScrollRange = recyclerView.computeVerticalScrollRange();
        final float rangeDiff = verticalScrollRange - mViewHeight;
        float proportion = (float) verticalScrollOffset / (rangeDiff > 0 ? rangeDiff : 1f);
        return mViewHeight * proportion;
    }

    private void setViewPositions(float y) {
        int handleY = Utility.getValueInRange(0, (int) (mViewHeight - mHandleView.getHeight()), (int) (y - mHandleView.getHeight() / 2));
        mBubbleView.setY(handleY + Utility.convertDpToPixel((56), ImagePicker.this));
        mHandleView.setY(handleY);
    }

    private void setRecyclerViewPosition(float y) {
        if (recyclerView != null && recyclerView.getAdapter() != null) {
            int itemCount = recyclerView.getAdapter().getItemCount();
            float proportion;

            if (mHandleView.getY() == 0) {
                proportion = 0f;
            } else if (mHandleView.getY() + mHandleView.getHeight() >= mViewHeight - sTrackSnapRange) {
                proportion = 1f;
            } else {
                proportion = y / mViewHeight;
            }

            int scrolledItemCount = Math.round(proportion * itemCount);
            int targetPos = Utility.getValueInRange(0, itemCount - 1, scrolledItemCount);
            recyclerView.getLayoutManager().scrollToPosition(targetPos);

            if (mainImageAdapter != null) {
                mBubbleView.setText(mainImageAdapter.getSectionMonthYearText(targetPos));
            }
        }
    }

    private void showBubble() {
        if (!Utility.isViewVisible(mBubbleView) && mainImageAdapter.getItemCount() > 0) {
            mBubbleView.setVisibility(View.VISIBLE);
            mBubbleView.setAlpha(0f);
            mBubbleAnimator = mBubbleView.animate().alpha(1f)
                    .setDuration(sBubbleAnimDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        // adapter required for new alpha value to stick
                    });
            mBubbleAnimator.start();
        }
    }

    private void hideBubble() {
        if (Utility.isViewVisible(mBubbleView)) {
            mBubbleAnimator = mBubbleView.animate().alpha(0f)
                    .setDuration(sBubbleAnimDuration)
                    .setListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mBubbleView.setVisibility(View.GONE);
                            mBubbleAnimator = null;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            super.onAnimationCancel(animation);
                            mBubbleView.setVisibility(View.GONE);
                            mBubbleAnimator = null;
                        }
                    });
            mBubbleAnimator.start();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < mHandleView.getX() - ViewCompat.getPaddingStart(mHandleView)) {
                    return false;
                }
                mHandleView.setSelected(true);
                handler.removeCallbacks(mScrollbarHider);
                Utility.cancelAnimation(mScrollbarAnimator);
                Utility.cancelAnimation(mBubbleAnimator);

                if (!Utility.isViewVisible(mScrollbar) && (recyclerView.computeVerticalScrollRange() - mViewHeight > 0)) {
                    mScrollbarAnimator = Utility.showScrollbar(mScrollbar, ImagePicker.this);
                }

                if (mainImageAdapter != null) {
                    showBubble();
                }

            case MotionEvent.ACTION_MOVE:
                final float y = event.getRawY();
                mBubbleView.setText(mainImageAdapter.getSectionMonthYearText(recyclerView.getVerticalScrollbarPosition()));
                setViewPositions(y - getSupportActionBar().getHeight());
                setRecyclerViewPosition(y);
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mHandleView.setSelected(false);
                if (mHideScrollbar) {
                    handler.postDelayed(mScrollbarHider, sScrollbarHideDelay);
                }
                hideBubble();
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            removePhotos(false, () -> super.onBackPressed());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == ImageEditor.RC_IMAGE_EDITOR) {
            returnObjects(data.getStringExtra(ImageEditor.EXTRA_EDITED_PATH));
        }
    }

    private void removePhotos(boolean ignoreSelected, Runnable runnable) {
        if (processing)
            return;
        Iterator<String> iterator = takenPhotos.iterator();
        while (iterator.hasNext()) {
            String path = iterator.next();
            if (ignoreSelected && containsSelectionList(path))
                iterator.remove();
            else
                new File(path).delete();
        }

        if (takenPhotos.isEmpty()) {
            runnable.run();
            return;
        }

        MediaScannerConnection.scanFile(getApplicationContext(), takenPhotos.toArray(new String[0]), null, (path, uri) -> runOnUiThread(runnable));
    }

    private boolean containsSelectionList(String path) {
        for (Img img : selectionList) {
            if (img.getUrl().equals(path))
                return true;
        }

        return false;
    }
}