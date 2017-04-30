package com.artemkopan.movie.util.extensions

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.annotation.DrawableRes
import android.support.annotation.Px
import android.widget.ImageView
import com.artemkopan.movie.BuildConfig
import com.artemkopan.movie.util.extensions.GlidePlaceHolder.Drawable
import com.artemkopan.movie.util.extensions.GlidePlaceHolder.Res
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import timber.log.Timber



private const val NO_OVERRIDE = -1

fun ImageView.loadImage(url: String?,
                        @Px width: Int = NO_OVERRIDE,
                        @Px height: Int = NO_OVERRIDE,
                        errorDrawable: GlidePlaceHolder = Drawable(),
                        placeholderDrawable: GlidePlaceHolder = Drawable(),
                        centerCrop: Boolean = true,
                        animate: Boolean = false,
                        requestListener: RequestListener<Any, GlideDrawable>? = if (BuildConfig.DEBUG) DebugRequestListener() else null,
                        vararg transformations: Transformation<Bitmap>) {

    val request = Glide.with(context).load(url)


    when (errorDrawable) {
        is Drawable -> request.error(errorDrawable.drawabable)
        is Res -> request.error(errorDrawable.res)
    }

    when (placeholderDrawable) {
        is Drawable -> request.placeholder(placeholderDrawable.drawabable)
        is Res -> request.placeholder(placeholderDrawable.res)
    }

    if (centerCrop && transformations.isEmpty()) {
        request.centerCrop()
    } else if (centerCrop && transformations.isNotEmpty()) {
        request.bitmapTransform(*arrayOf(CenterCrop(context), *transformations))
    } else if (transformations.isNotEmpty()) {
        request.bitmapTransform(*transformations)
    }

    if (!animate) {
        request.dontAnimate()
    }

    if (width != NO_OVERRIDE && height != NO_OVERRIDE) {
        request.override(width, height)
    }

    if (requestListener != null) request.listener(requestListener)

    request.into(this)
}

class DebugRequestListener : RequestListener<Any, GlideDrawable> {
    override fun onException(e: Exception?, model: Any?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
        Timber.d("onException() called with: e = [$e], model = [$model], target = [$target], isFirstResource = [$isFirstResource]")
        return false
    }

    override fun onResourceReady(resource: GlideDrawable?, model: Any?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
        Timber.d("onResourceReady() called with: resource = [$resource], model = [$model], target = [$target], isFromMemoryCache = [$isFromMemoryCache], isFirstResource = [$isFirstResource]")
        return false
    }
}

sealed class GlidePlaceHolder {
    class Drawable(val drawabable: android.graphics.drawable.Drawable = ColorDrawable(Color.GRAY)) : GlidePlaceHolder()
    class Res(@DrawableRes val res: Int) : GlidePlaceHolder()

    object Nothing : GlidePlaceHolder()
}