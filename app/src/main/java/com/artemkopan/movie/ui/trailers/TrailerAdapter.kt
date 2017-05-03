package com.artemkopan.movie.ui.trailers

import android.view.ViewGroup
import com.artemkopan.movie.R
import com.artemkopan.movie.data.entity.VideoItem
import com.artemkopan.recycler.adapter.RecyclerBaseAdapter
import com.artemkopan.utils.ViewUtils
import com.artemkopan.utils.rx.RxViewClick
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.item_trailer.view.*
import javax.inject.Inject

/**
 * Created by Artem Kopan for TheMovie
 * 02.05.2017
 */

class TrailerAdapter @Inject constructor() : RecyclerBaseAdapter<VideoItem, TrailerHolder>() {

    @Inject lateinit var destory: CompositeDisposable

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TrailerHolder {
        val holder = TrailerHolder(ViewUtils.inflateView(p0, R.layout.item_trailer))
        holder.itemView.layoutParams.width = (p0.width * 0.8f).toInt()
        RxViewClick.create(holder.itemView.titleTxt)
                .subscribe { callOnItemClick(holder, it) }
                .addTo(destory)

        return holder
    }

    override fun onBindViewHolder(p0: TrailerHolder, p1: VideoItem?, p2: Int) {
        p0.bind(p1!!, p2)
    }
}
