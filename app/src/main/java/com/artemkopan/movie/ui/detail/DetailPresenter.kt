package com.artemkopan.movie.ui.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import com.artemkopan.movie.R
import com.artemkopan.movie.data.database.DatabaseManager
import com.artemkopan.movie.data.entity.DetailItem
import com.artemkopan.movie.data.entity.MovieItem
import com.artemkopan.movie.data.exception.ExceptionManager
import com.artemkopan.movie.data.model.AppendResponseBuilder
import com.artemkopan.movie.data.model.AppendType.IMAGES
import com.artemkopan.movie.data.model.AppendType.VIDEOS
import com.artemkopan.movie.data.model.movie.MovieModel
import com.artemkopan.movie.injection.qualifer.ApplicationContext
import com.artemkopan.movie.ui.trailers.TrailerAdapter
import com.artemkopan.movie.util.CommonUtils
import com.artemkopan.movie.util.SpaceItemDecoration
import com.artemkopan.movie.util.extensions.applySchedulers
import com.artemkopan.mvp.presenter.BasePresenterImpl
import com.artemkopan.utils.CollectionUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class DetailPresenter @Inject constructor() : BasePresenterImpl<DetailView>() {

    @Inject lateinit var model: MovieModel
    @Inject lateinit var destroy: CompositeDisposable
    @Inject lateinit var res: Resources
    @Inject lateinit var trailersAdapter: TrailerAdapter
    @Inject lateinit var database: DatabaseManager
    @Inject @field:ApplicationContext lateinit var context: Context

    private val apiKey by lazy { res.getString(R.string.api_key) }
    private var item: DetailItem? = null
    private var changed = false

    fun loadDetail(id: Long) {
        //load movies
        model.getMovieDetail(apiKey, id, AppendResponseBuilder().add(VIDEOS).add(IMAGES))
                .map { it }
                .flatMap { detail ->
                    //check exist in DB and set favorite
                    return@flatMap database.findMovies(detail.id)
                            .doOnSuccess { detail?.isFavorite = !CollectionUtils.isEmpty(it) }
                            .map { return@map detail }
                }
                .applySchedulers()
                .doOnSubscribe(this::onShowProgress)
                .doFinally(this::onHideProgress)
                .subscribe(Consumer {
                    item = it
                    trailersAdapter.list = it.videos?.results!!
                    mvpView?.setDetail(it)
                    mvpView?.toggleFavorite(it.isFavorite)
                }, ExceptionManager.consumerThrowable({ mvpView }))
                .addTo(destroy)
    }

    fun setTrailersList(trailersList: RecyclerView) {
        val spacing = res.getDimensionPixelSize(R.dimen.base_margin)
        trailersList.addItemDecoration(SpaceItemDecoration(spacing, true, true, true))
        trailersList.isNestedScrollingEnabled = false

        val snapping = LinearSnapHelper()
        snapping.attachToRecyclerView(trailersList)
        trailersList.adapter = trailersAdapter
        trailersAdapter.setOnItemClickListener { _, _, video, _ -> CommonUtils.openYoutubeVideo(context, video.key ?: "") }
    }

    fun addOrRemoveFavorite() {
        if (item == null) return
        changed = true
        if (item!!.isFavorite) {
            database.removeItems(MovieItem::class.java, { it.equalTo("id", item!!.id) })
                    .subscribe(Consumer { item!!.isFavorite = false; mvpView?.toggleFavorite(false) },
                               ExceptionManager.consumerThrowable({ mvpView }))
        } else {
            database.createOrUpdate(MovieItem(item!!))
                    .subscribe(Consumer { item!!.isFavorite = true; mvpView?.toggleFavorite(true) },
                               ExceptionManager.consumerThrowable({ mvpView }))

        }
    }

    fun onBackPressed(detailActivity: DetailActivity) {
        val intent = Intent()
        intent.putExtra(DetailActivity.KEY_UPDATE_FAVORITES, changed)
        detailActivity.setResult(Activity.RESULT_OK, intent)
    }


}
