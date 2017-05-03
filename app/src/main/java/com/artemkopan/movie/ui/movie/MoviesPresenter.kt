package com.artemkopan.movie.ui.movie

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.util.Pair
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup
import android.view.View
import com.artemkopan.movie.R
import com.artemkopan.movie.data.database.DatabaseManager
import com.artemkopan.movie.data.entity.MovieItem
import com.artemkopan.movie.data.entity.MovieResponse
import com.artemkopan.movie.data.exception.ExceptionManager
import com.artemkopan.movie.data.model.movie.MovieModel
import com.artemkopan.movie.injection.qualifer.ActivityContext
import com.artemkopan.movie.injection.scope.PresentationScope
import com.artemkopan.movie.ui.detail.DetailActivity
import com.artemkopan.movie.util.Pagination
import com.artemkopan.mvp.presenter.BasePresenterImpl
import com.artemkopan.recycler.listeners.OnItemClickListener
import com.artemkopan.recycler.listeners.OnRecyclerPaginationListener
import com.artemkopan.recycler.listeners.OnRecyclerPaginationListener.OnRecyclerPaginationResult
import com.artemkopan.recycler.view.ExRecyclerView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_movies.*
import javax.inject.Inject

@PresentationScope
class MoviesPresenter @Inject constructor() : BasePresenterImpl<MoviesView>(), OnRecyclerPaginationResult, OnItemClickListener<MovieItem> {

    @Inject lateinit var model: MovieModel
    @Inject lateinit var destroy: CompositeDisposable
    @Inject lateinit var adapter: MoviesAdapter
    @Inject lateinit var database: DatabaseManager
    @Inject @field:ActivityContext lateinit var activity: FragmentActivity

    companion object {
        internal const val NONE = -1
        internal const val POPULAR = 1
        internal const val TOP_RATED = 2
        internal const val FAVORITE = 3
    }

    private val apiKey by lazy { activity.getString(R.string.api_key) }
    private val pagination: Pagination by lazy { Pagination() }
    private var paginationListener: OnRecyclerPaginationListener? = null
    private var type = NONE

    override fun attachView(view: MoviesView?) {
        super.attachView(view)
        adapter.setOnItemClickListener(this)
    }

    fun setList(list: ExRecyclerView) {
        list.adapter = adapter
        paginationListener = list.createPaginationListener(this)

        list.layoutManager.let {
            if (it is GridLayoutManager) {
                it.spanSizeLookup = object : SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        // show progress with fill width
                        if (position == adapter.listSize) return it.spanCount
                        // else return default span size
                        return 1
                    }
                }
            }
        }
    }

    fun setType(type: Int) {
        if (this.type == type) return
        this.type = type
        adapter.list.beginBatchedUpdates()
        adapter.list.clear()
        pagination.reset()
        when (type) {
            POPULAR -> {
                loadPopular(); mvpView?.updateTitle(R.string.action_most_popular)
            }
            TOP_RATED -> {
                loadTopRated(); mvpView?.updateTitle(R.string.action_top_rated)
            }
            FAVORITE -> {
                loadFavorite(); mvpView?.updateTitle(R.string.action_favorite)
            }
        }
        adapter.list.endBatchedUpdates()
    }

    override fun onRecyclePaginationNextPage() {
        paginationListener?.disablePagination()
        if (pagination.hasNext()) {
            when (type) {
                POPULAR -> loadPopular(pagination.next())
                TOP_RATED -> loadTopRated(pagination.next())
            }
        }
    }

    override fun onItemClickListener(view: View, p1: Int, item: MovieItem?, vararg shared: View?) {
        if (view.id == R.id.posterImg && item != null) {
            val toolbar = Pair<View, String>(activity.appBar, activity.getString(R.string.transition_app_bar))
            val poster = Pair<View, String>(shared[0], activity.getString(R.string.transition_app_bar))
            DetailActivity.route(item).startWithTransition(activity, toolbar, poster)
        }
    }

    private fun loadFavorite() {
        paginationListener?.disablePagination()
        database.findItems(MovieItem::class.java, {})
                .subscribe(Consumer { adapter.list.addAll(it); mvpView?.showEmptyList(adapter.isEmpty) },
                           ExceptionManager.consumerThrowable({ mvpView }))
    }

    private fun loadTopRated(page: Int = pagination.page) {
        load(model.getTopRated(apiKey, page)
                     .doOnSuccess(updatePagination())
                     .map { it.results })
    }

    private fun loadPopular(page: Int = pagination.page) {
        load(model.getPopular(apiKey, page)
                     .doOnSuccess(updatePagination())
                     .map { it.results })
    }

    private fun load(observer: Single<List<MovieItem>>) {
        observer
                .doOnError { paginationListener?.enablePagination() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { if (adapter.isEmpty) onShowProgress() else adapter.showFooter(true) }
                .doFinally { onHideProgress(); adapter.showFooter(false); }
                .subscribe(Consumer { adapter.list.addAll(it); mvpView?.showEmptyList(adapter.isEmpty) },
                           ExceptionManager.consumerThrowable({ mvpView }))
                .addTo(destroy)
    }

    private fun updatePagination(): Consumer<in MovieResponse<List<MovieItem>>>? {
        return Consumer {
            pagination.page = it.page ?: Pagination.DEFAULT_VAL
            pagination.total = it.totalPages ?: Pagination.DEFAULT_VAL
            if (pagination.hasNext()) paginationListener?.enablePagination()
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == DetailActivity.REQ && resultCode == Activity.RESULT_OK && type == FAVORITE) {
            if (data?.getBooleanExtra(DetailActivity.KEY_UPDATE_FAVORITES, false) ?: false) {
                adapter.clear()
                loadFavorite()
            }
        }
    }


}