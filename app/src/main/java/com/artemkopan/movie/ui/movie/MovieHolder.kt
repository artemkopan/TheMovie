package com.artemkopan.movie.ui.movie

import android.content.Context
import android.support.v4.view.ViewCompat
import android.view.View
import android.widget.ImageView
import com.artemkopan.movie.R.drawable
import com.artemkopan.movie.data.entity.MovieItem
import com.artemkopan.movie.util.extensions.GlidePlaceHolder.Res
import com.artemkopan.movie.util.extensions.loadCancel
import com.artemkopan.movie.util.extensions.loadImage
import com.artemkopan.movie.util.glide.models.PosterImage
import com.artemkopan.recycler.holder.BaseHolder
import com.bumptech.glide.load.engine.DiskCacheStrategy.ALL

/**
 * Created by Artem Kopan for TheMovie
 * 30.04.2017
 */
class MovieHolder(itemView: View) : BaseHolder<MovieItem>(itemView) {

    override fun bind(context: Context, item: MovieItem, position: Int) {
        (itemView as ImageView).loadImage(PosterImage(item.posterPath),
                                          thumbnail = 0.1f,
                                          errorDrawable = Res(drawable.ic_error),
                                          diskStrategy = ALL,
                                          placeholderDrawable = Res(drawable.ic_placeholder))
        ViewCompat.setTransitionName(itemView, "poster" + position)
    }

    override fun clear() {
        (itemView as ImageView).loadCancel()
    }
}