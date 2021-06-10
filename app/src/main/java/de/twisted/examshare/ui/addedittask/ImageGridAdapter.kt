package de.twisted.examshare.ui.addedittask

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import de.twisted.examshare.R
import de.twisted.examshare.databinding.ItemGridImageBinding
import de.twisted.examshare.databinding.ItemTaskBinding
import java.io.File

class ImageGridAdapter(
    private val context: Context,
    private val paths: List<String>
) : RecyclerView.Adapter<ImageViewHolder>() {

    private var imageSize = 0

    init {
        setColumnNumber(context, 3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemGridImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding, imageSize)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(paths[position])
    }

    override fun getItemCount(): Int = paths.size

    private fun setColumnNumber(context: Context, columnNumber: Int) {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(metrics)
        imageSize = metrics.widthPixels / columnNumber
    }
}

class ImageViewHolder(
    private val binding: ItemGridImageBinding,
    private val imageSize: Int
) : RecyclerView.ViewHolder(binding.root) {

    private val imageView: ImageView = binding.image

    fun bind(imagePath: String) {
        Glide.with(imageView.context)
            .load(File(imagePath))
            .apply(RequestOptions.centerCropTransform()
                .dontAnimate()
                .override(imageSize, imageSize)
                .placeholder(R.drawable.image_placeholder))
            .thumbnail(0.65f)
            .into(imageView)
    }
}