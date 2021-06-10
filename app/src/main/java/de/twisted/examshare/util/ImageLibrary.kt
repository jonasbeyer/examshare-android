package de.twisted.examshare.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.webkit.MimeTypeMap
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import java.io.File
import java.util.*

object ImageLibrary {
    private var bitmap: Bitmap? = null

    @JvmStatic
    fun getBitmap(): Bitmap? = bitmap

    @JvmStatic
    fun setBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
    }

    @JvmStatic
    fun getBitmap(context: Context, drawableId: Int, width: Int, height: Int): Bitmap {
        val drawable: Drawable? = ContextCompat.getDrawable(context, drawableId)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable!!.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    @JvmStatic
    fun getMimeType(file: File): String? {
        if (!file.exists()) return null
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.absolutePath.toLowerCase(Locale.ROOT))
        if (extension != null) type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        return type
    }
}

enum class ImageType(val typeName: String) {
    TASK_IMAGE("taskImages"),
    SOLUTION_IMAGE("solutionImages");
}