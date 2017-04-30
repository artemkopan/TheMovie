package com.artemkopan.movie.ui.movie

import android.content.Context
import android.support.v7.util.DiffUtil
import com.artemkopan.movie.R
import com.artemkopan.movie.data.entity.Movie
import com.artemkopan.movie.data.exception.ExceptionManager
import com.artemkopan.movie.data.model.movie.MovieModel
import com.artemkopan.movie.injection.qualifer.ApplicationContext
import com.artemkopan.movie.injection.scope.PresentationScope
import com.artemkopan.movie.util.Pagination
import com.artemkopan.mvp.presenter.BasePresenterImpl
import com.artemkopan.recycler.listeners.OnRecyclerPaginationListener
import com.artemkopan.recycler.listeners.OnRecyclerPaginationListener.OnRecyclerPaginationResult
import com.artemkopan.recycler.view.ExRecyclerView
import com.artemkopan.utils.ExtraUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

@PresentationScope
class MoviesPresenter @Inject constructor() : BasePresenterImpl<MoviesView>(), OnRecyclerPaginationResult {

    @Inject lateinit var model: MovieModel
    @Inject lateinit var destroy: CompositeDisposable
    @Inject lateinit var adapter: MoviesAdapter
    @Inject @field:ApplicationContext lateinit var context: Context

    private val apiKey: String by lazy { context.getString(R.string.api_key) }
    private val paginationPopular: Pagination by lazy { Pagination() }
    private var paginationListener: OnRecyclerPaginationListener? = null

    fun setList(list: ExRecyclerView) {
        list.adapter = adapter
        paginationListener = list.createPaginationListener(this)

        if (adapter.isEmpty) {
            paginationPopular.reset()
            loadPopular()
        }
    }

    private fun loadPopular(page: Int = paginationPopular.page) {
        load(model.getPopular(apiKey, page)
                     .doOnSuccess {
                         paginationPopular.page = it.page ?: 1
                         paginationPopular.total = it.totalPages ?: 1
                         if (paginationPopular.hasNext()) paginationListener?.enablePagination()
                     }
                     .map { it.results })
    }

    private fun load(observer: Single<List<Movie>>) {
        observer
                .map {
                    ExtraUtils.checkBackgroundThread()
                    val newList = ArrayList<Movie>(adapter.listSize + it.size)
                    newList.addAll(adapter.list)
                    newList.addAll(it)
                    val diff = DiffUtil.calculateDiff(MoviesAdapter.MoviesCallback(adapter.list, newList))
                    adapter.setList(newList, false)
                    return@map diff
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onShowProgress() }
                .doFinally { onHideProgress() }
                .subscribe(Consumer { it.dispatchUpdatesTo(adapter) },
                           ExceptionManager.consumerThrowable({ mvpView }))
                .addTo(destroy)
    }

    override fun onRecyclePaginationNextPage() {
        if (paginationPopular.hasNext()) {
            loadPopular(paginationPopular.next())
        }
    }

}