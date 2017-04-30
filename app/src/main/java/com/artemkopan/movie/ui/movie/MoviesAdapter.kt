package com.artemkopan.movie.ui.movie

import android.content.Context
import android.view.ViewGroup
import com.artemkopan.movie.R
import com.artemkopan.movie.data.entity.Movie
import com.artemkopan.movie.injection.qualifer.ApplicationContext
import com.artemkopan.recycler.adapter.RecyclerBaseAdapter
import com.artemkopan.recycler.diff.BaseDiffCallback
import com.artemkopan.utils.ObjectUtils
import com.artemkopan.utils.ViewUtils
import javax.inject.Inject

/**
 * Created by Artem Kopan for TheMovie
 * 30.04.2017
 */
class MoviesAdapter @Inject constructor() : RecyclerBaseAdapter<Movie, MovieHolder>() {

    @Inject @field:ApplicationContext lateinit var context: Context

    init {
        createList(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        return MovieHolder(ViewUtils.inflateView(parent, R.layout.item_movie))
    }

    override fun onBindViewHolder(holder: MovieHolder, model: Movie, position: Int) {
//        holder.bind(context, model, position)
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
