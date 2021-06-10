package de.twisted.examshare.ui.taskdetails

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import de.twisted.examshare.R
import de.twisted.examshare.databinding.ActivityTaskFullImageBinding
import de.twisted.examshare.ui.shared.base.ExamActivity
import de.twisted.examshare.util.ImageLibrary
import de.twisted.examshare.util.extensions.showToast
import de.twisted.examshare.util.helper.TextUtil
import de.twisted.imagepicker.utility.PermUtil
import de.twisted.imagepicker.utility.Utility
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class TaskFullImageActivity : ExamActivity() {

    private var isFullscreen = false

    private lateinit var binding: ActivityTaskFullImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ImageLibrary.getBitmap() == null) {
            Timber.e("Image not available")
            finish()
        }

        hideNavigationBar()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_full_image)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        binding.photoView.apply {
            displayType = ImageViewTouchBase.DisplayType.FIT_TO_SCREEN
            setImageBitmap(ImageLibrary.getBitmap())
            setSingleTapListener { setFullscreenMode(!isFullscreen) }
            setDoubleTapListener { setFullscreenMode(!isFullscreen) }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_task_full_image, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_item_rotate_image -> {
            ImageLibrary.setBitmap(Utility.rotate(ImageLibrary.getBitmap(), 90))
            updateCurrentImage(ImageLibrary.getBitmap()!!)
            true
        }
        R.id.menu_item_share_image -> {
            shareImage()
            true
        }
        R.id.menu_item_save_image -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PermUtil.checkForPermissions(this, false) { saveImageExternal() }
            } else {
                saveImageExternal()
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideNavigationBar()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS)
            return

        for (i in 0 until permissions.size) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                return
        }

        this.saveImageExternal();
    }

    private fun updateCurrentImage(bitmap: Bitmap) {
        binding.photoView.setImageBitmap(bitmap)
    }

    private fun hideNavigationBar() {
        val visibilityFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            .or(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
            .or(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
            .or(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        window.decorView.systemUiVisibility = visibilityFlags
    }

    private fun saveImageExternal() {
        val file = saveImageToFile(ImageLibrary.getBitmap()!!, cache = false, dateName = true)
        Utility.refreshImage(this, file) {
            showToast(R.string.image_saved)
        }
    }

    private fun shareImage() {
        this.saveImageToFile(ImageLibrary.getBitmap()!!, cache = true, dateName = false);
        val uri = FileProvider.getUriForFile(this, "de.twisted.examshare.fileprovider", File("$cacheDir/images/image.jpg"))

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        startActivity(Intent.createChooser(intent, getString(R.string.share_action)))
    }


    private fun saveImageToFile(bitmap: Bitmap, cache: Boolean, dateName: Boolean): File {
        val path = "Pictures/ExamShare"
        val dir = if (cache) {
            File(cacheDir, "images")
        } else {
            File(Environment.getExternalStorageDirectory(), path)
        }

        if (!dir.exists()) {
            dir.mkdir()
        }

        val name = if (dateName) {
            "IMG_${SimpleDateFormat("yyyyMMdd_HHmmSS", Locale.ENGLISH).format(Date())}"
        } else {
            "image"
        }

        val photo = File(dir, "$name.jpg");

        try {
            val fileOutputStream = FileOutputStream(photo.path)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return photo;
    }

    private fun setFullscreenMode(isFullscreen: Boolean) {
        val flag = WindowManager.LayoutParams.FLAG_FULLSCREEN

        this.isFullscreen = isFullscreen

        if (isFullscreen) {
            supportActionBar?.hide()
            window.addFlags(flag)
        } else {
            supportActionBar?.show()
            window.clearFlags(flag)
        }
    }
}