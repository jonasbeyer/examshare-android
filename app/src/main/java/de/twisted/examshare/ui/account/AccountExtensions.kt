package de.twisted.examshare.ui.account

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import de.twisted.examshare.BuildConfig
import de.twisted.examshare.R

fun setProfileAvatar(
    context: Context,
    target: ImageView,
    userId: String,
    placeholder: Int = R.drawable.ic_account_circle_grey
) {
    val imageUrl = "${BuildConfig.API_ENDPOINT}users/me/profileImage"
    Glide.with(context)
        .load(imageUrl)
        .apply(RequestOptions
            .placeholderOf(placeholder)
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
        )
        .into(target)
}