package com.artemkopan.movie.ui.detail

import android.content.res.Resources
import com.artemkopan.movie.R
import com.artemkopan.movie.data.exception.ExceptionManager
import com.artemkopan.movie.data.model.AppendResponseBuilder
import com.artemkopan.movie.data.model.AppendType.IMAGES
import com.artemkopan.movie.data.model.AppendType.VIDEOS
import com.artemkopan.movie.data.model.movie.MovieModel
import com.artemkopan.movie.util.extensions.applySchedulers
import com.artemkopan.mvp.presenter.BasePresenterImpl
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class DetailPresenter @Inject constructor() : BasePresenterImpl<DetailView>() {

    @Inject lateinit var model: MovieModel
    @Inject lateinit var destroy: CompositeDisposable
    @Inject lateinit var res: Resources

    private val apiKey by lazy { res.getString(R.string.api_key) }

    fun loadDetail(id: Int) {
        model.getMovieDetail(apiKey, id, AppendResponseBuilder().add(VIDEOS).add(IMAGES))
                .map { it }
                .applySchedulers()
                .doOnSubscribe(this::onShowProgress)
                .doFinally(this::onHideProgress)
                .subscribe(Consumer { mvpView?.setDetail(it) }, ExceptionManager.consumerThrowable({ mvpView }))
                .addTo(destroy)
    }

}
