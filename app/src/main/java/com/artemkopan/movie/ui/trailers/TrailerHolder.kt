package com.artemkopan.movie.ui.trailers

import android.content.Context
import android.view.View
import com.artemkopan.movie.R
import com.artemkopan.movie.data.entity.VideoItem
import com.artemkopan.movie.util.extensions.GlidePlaceHolder.Nothing
import com.artemkopan.movie.util.extensions.loadCancel
import com.artemkopan.movie.util.extensions.loadImage
import com.artemkopan.movie.util.glide.RoundedCornersTransformation
import com.artemkopan.movie.util.glide.models.YoutubeThumbnail
import com.artemkopan.recycler.holder.BaseHolder
import kotlinx.android.synthetic.main.item_trailer.view.*

/**
 * Created by Artem Kopan for TheMovie
 * 02.05.2017
 */
class TrailerHolder(itemView: View) : BaseHolder<VideoItem>(itemView) {


    override fun bind(p0: Context, item: VideoItem?, pos: Int) {
        itemView.thumbnail.loadImage(
                YoutubeThumbnail(item?.key ?: ""),
                placeholderDrawable = Nothing,
                errorDrawable = Nothing,
                transformations = RoundedCornersTransformation(p0, p0.resources.getDimensionPixelSize(R.dimen.corner_radius), 0))
        itemView.titleTxt.text = item?.name
    }

    override fun clear() {
        itemView.thumbnail.loadCancel()
    }

}