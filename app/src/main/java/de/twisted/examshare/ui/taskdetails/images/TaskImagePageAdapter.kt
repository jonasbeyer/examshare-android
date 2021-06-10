package de.twisted.examshare.ui.taskdetails.images

import android.graphics.Bitmap
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import de.twisted.examshare.BuildConfig
import de.twisted.examshare.data.models.Task
import de.twisted.examshare.databinding.ItemImagePageBinding
import de.twisted.examshare.util.ImageLibrary
import de.twisted.examshare.util.ImageType

class TaskImagePageAdapter(
    private val task: Task,
    private val imageType: ImageType
) : RecyclerView.Adapter<TaskImageViewHolder>() {

    val bitmapList = SparseArray<Bitmap>()
    var imageLoadedListener: TaskImagesFragment.Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskImageViewHolder {
        val binding = ItemImagePageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskImageViewHolder(binding)
    }

    override fun getItemCount(): Int = when (imageType) {
        ImageType.TASK_IMAGE -> task.taskImagesCount
        ImageType.SOLUTION_IMAGE -> task.solutionImagesCount
    }

    override fun onBindViewHolder(holder: TaskImageViewHolder, position: Int) {
        holder.bind(buildImageUrl(position)) { bitmap ->
            imageLoadedListener?.onImageLoaded()
            bitmapList.put(position, bitmap)
        }
    }

    private fun buildImageUrl(position: Int): String {
        return "${BuildConfig.API_ENDPOINT}tasks/${task.id}/images/${imageType.typeName}/$position"
    }
}

class TaskImageViewHolder(
    private val binding: ItemImagePageBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val imageView = binding.imageView

    fun bind(imageUrl: String, onResourceReady: (bitmap: Bitmap) -> Unit) {
        Glide.with(imageView.context)
            .asBitmap()
            .load(imageUrl)
            .apply(RequestOptions()
//                .diskCacheStrategy(if (Preferences.isOfflineCache()) DiskCacheStrategy.ALL else DiskCacheStrategy.NONE)
                .override(SIZE_ORIGINAL)
                .fitCenter())
            .transition(BitmapTransitionOptions.withCrossFade())
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                    binding.apply {
                        errorView.isVisible = true
                        progressBar.isVisible = false
                    }

                    return true
                }

                override fun onResourceReady(resource: Bitmap, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    binding.progressBar.isVisible = false
                    onResourceReady(resource)
                    return false
                }
            })
            .into(imageView)
    }
}