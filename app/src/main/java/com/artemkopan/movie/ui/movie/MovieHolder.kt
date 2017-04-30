package com.artemkopan.movie.ui.movie

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.artemkopan.movie.data.entity.Movie
import com.artemkopan.movie.util.extensions.loadCancel
import com.artemkopan.movie.util.extensions.loadImage
import com.artemkopan.movie.util.glide.PosterImage
import com.artemkopan.recycler.holder.BaseHolder

/**
 * Created by Artem Kopan for TheMovie
 * 30.04.2017
 */
class MovieHolder(itemView: View) : BaseHolder<Movie>(itemView) {

    override fun bind(context: Context, item: Movie, position: Int) {
        (itemView as ImageView).loadImage(PosterImage(item.posterPath))
    }

    override fun clear() {
        (itemView as ImageView).loadCancel()
    }
}