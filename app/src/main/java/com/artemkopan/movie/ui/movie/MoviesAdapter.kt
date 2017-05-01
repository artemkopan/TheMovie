package com.artemkopan.movie.ui.movie

import android.content.Context
import android.view.ViewGroup
import com.artemkopan.movie.R
import com.artemkopan.movie.data.entity.Movie
import com.artemkopan.movie.injection.qualifer.ApplicationContext
import com.artemkopan.recycler.adapter.RecyclerAdapter
import com.artemkopan.recycler.adapter.RecyclerSortedAdapter
import com.artemkopan.recycler.adapter.RecyclerSortedCallback
import com.artemkopan.recycler.diff.BaseDiffCallback
import com.artemkopan.recycler.holder.BaseHolder
import com.artemkopan.recycler.holder.SimpleHolder
import com.artemkopan.utils.ObjectUtils
import com.artemkopan.utils.ViewUtils
import com.artemkopan.utils.rx.RxViewClick
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

/**
 * Created by Artem Kopan for TheMovie
 * 30.04.2017
 */
class MoviesAdapter @Inject constructor() : RecyclerSortedAdapter<Movie, BaseHolder<Movie>>() {

    @Inject lateinit var destroy: CompositeDisposable
    @Inject @field:ApplicationContext lateinit var context: Context

    init {
        initList(Movie::class.java, object : RecyclerSortedCallback<Movie>(this) {
            override fun compare(o1: Movie?, o2: Movie?): Int = 0

            override fun areContentsTheSame(oldItem: Movie?, newItem: Movie?): Boolean {
                return ObjectUtils.equalsObject(oldItem?.posterPath, newItem?.posterPath)
            }

            override fun areItemsTheSame(item1: Movie?, item2: Movie?): Boolean {
                return ObjectUtils.equalsObject(item1?.id, item2?.id)
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<Movie> {
        when (viewType) {
            RecyclerAdapter.FOOTER -> return SimpleHolder<Movie>(ViewUtils.inflateView(parent, R.layout.item_progress))
            else -> {
                val holder = MovieHolder(ViewUtils.inflateView(parent, R.layout.item_movie))
                RxViewClick.create(holder.itemView).subscribe { callOnItemClick(holder, it, it) }.addTo(destroy)
                return holder
            }
        }
    }

    override fun onBindViewHolder(holder: BaseHolder<Movie>?, model: Movie?, position: Int) {
        holder?.bind(context, model, position)
    }

    class MoviesCallback(oldList: List<Movie>, newList: List<Movie>) : BaseDiffCallback<Movie>(oldList, newList) {

        override fun areContentsTheSame(oldItem: Movie?, newItem: Movie?): Boolean {
            return true
        }

        override fun areItemsTheSame(oldItem: Movie?, newItem: Movie?): Boolean {
            return ObjectUtils.equalsObject(oldItem?.id, newItem?.id)
        }
    }
}
