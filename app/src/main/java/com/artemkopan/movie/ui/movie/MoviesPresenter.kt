package com.artemkopan.movie.ui.movie

import android.support.v4.app.FragmentActivity
import android.support.v4.util.Pair
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup
import android.view.View
import com.artemkopan.movie.R
import com.artemkopan.movie.data.entity.Movie
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
class MoviesPresenter @Inject constructor() : BasePresenterImpl<MoviesView>(), OnRecyclerPaginationResult, OnItemClickListener<Movie> {

    @Inject lateinit var model: MovieModel
    @Inject lateinit var destroy: CompositeDisposable
    @Inject lateinit var adapter: MoviesAdapter
    @Inject @field:ActivityContext lateinit var activity: FragmentActivity

    private val apiKey by lazy { activity.getString(R.string.api_key) }
    private val spanCount by lazy { activity.resources.getInteger(R.integer.movies_span_count) }
    private val paginationPopular: Pagination by lazy { Pagination() }
    private var paginationListener: OnRecyclerPaginationListener? = null

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
                        if (position == adapter.listSize) return spanCount
                        // else return default span size
                        return 1
                    }
                }
            }
        }

        if (adapter.isEmpty) {
            paginationPopular.reset()
            loadPopular()
        }
    }

    override fun onRecyclePaginationNextPage() {
        paginationListener?.disablePagination()
        if (paginationPopular.hasNext()) {
            loadPopular(paginationPopular.next())
        }
    }

    override fun onItemClickListener(view: View, p1: Int, item: Movie?, vararg shared: View?) {
        if (view.id == R.id.posterImg && item != null) {
            val toolbar = Pair<View, String>(activity.appBar, activity.getString(R.string.transition_app_bar))
            val poster = Pair<View, String>(shared[0], activity.getString(R.string.transition_app_bar))
            DetailActivity.route(item).startWithTransition(activity, toolbar, poster)
        }
    }

    private fun loadPopular(page: Int = paginationPopular.page) {
        load(model.getPopular(apiKey, page)
                     .doOnSuccess {
                         paginationPopular.page = it.page ?: Pagination.DEFAULT_VAL
                         paginationPopular.total = it.totalPages ?: Pagination.DEFAULT_VAL
                         if (paginationPopular.hasNext()) paginationListener?.enablePagination()
                     }
                     .doOnError { paginationListener?.enablePagination() }
                     .map { it.results })
    }

    private fun load(observer: Single<List<Movie>>) {
        observer
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { if (adapter.isEmpty) onShowProgress() else adapter.showFooter(true) }
                .doFinally { onHideProgress(); adapter.showFooter(false); }
                .subscribe(Consumer { adapter.list.addAll(it) }, ExceptionManager.consumerThrowable({ mvpView }))
                .addTo(destroy)
    }


}