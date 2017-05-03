package com.artemkopan.movie.ui.detail

import android.graphics.drawable.ColorDrawable
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import com.artemkopan.movie.R
import com.artemkopan.movie.data.entity.DetailItem
import com.artemkopan.movie.data.entity.MovieItem
import com.artemkopan.movie.ui.base.BaseInjectActivity
import com.artemkopan.movie.util.CommonUtils
import com.artemkopan.movie.util.extensions.GlidePlaceHolder.Drawable
import com.artemkopan.movie.util.extensions.loadImage
import com.artemkopan.movie.util.extensions.setGone
import com.artemkopan.movie.util.glide.models.PosterImage
import com.artemkopan.utils.CollectionUtils
import com.artemkopan.utils.animations.AnimUtils
import com.artemkopan.utils.router.ActivityBuilder
import com.artemkopan.utils.router.Router
import com.artemkopan.utils.rx.RxViewClick
import com.artemkopan.utils.transitions.TransitionHelper
import com.artemkopan.widget.fonts.FontUtils
import com.bumptech.glide.load.engine.DiskCacheStrategy.RESULT
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.layout_detail.*
import javax.inject.Inject


class DetailActivity : BaseInjectActivity<DetailPresenter, DetailView>(), DetailView {


    companion object {
        const val REQ = 132
        const val KEY_UPDATE_FAVORITES = "KEY_UPDATE_FAVORITES"

        private const val KEY_MOVIE = "KEY_MOVIE"

        fun route(movie: MovieItem): ActivityBuilder<*> {
            return Router.activity(DetailActivity::class.java).putExtra(KEY_MOVIE, movie).returnResult(REQ)
        }

    }

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        TransitionHelper.waitStartEnterTransition(this)
        val movie = intent.getParcelableExtra<MovieItem>(KEY_MOVIE)
        if (movie == null) {
            supportFinishAfterTransition()
            return
        }
        setToolbar(movie)
        setPoster(movie)

        // load data after transition ended
        TransitionHelper.onEnterSharedTransitionEndAction(this, {
            ViewCompat.setTransitionName(appBar, "")
            injectAsync({ it.inject(this) }, {
                presenter.loadDetail(movie.id)
                presenter.setTrailersList(trailersList)
            })
            RxViewClick.create(favoriteBtn)
                    .subscribe {
                        favoriteBtn.isEnabled = false
                        presenter.addOrRemoveFavorite()
                    }.addTo(onDestroyDisposable)
        })

        genreTxt.isSelected = true
    }


    override fun onInflateLayout(): Int = com.artemkopan.movie.R.layout.activity_detail

    @Inject
    override fun injectPresenter(presenter: DetailPresenter) {
        super.injectPresenter(presenter)
    }

    override fun showProgress(tag: Any?) {
        progressBar setGone false
    }

    override fun hideProgress(tag: Any?) {
        progressBar setGone true
    }

    override fun showError(tag: Any?, error: String?) {
        super.showError(tag, error)
    }

    private fun setToolbar(movie: MovieItem) {
        onToolbarInit(R.id.toolbar, R.drawable.ic_arrow_back_white_24dp)
        onToolbarNavigationClickListener { onBackPressed() }
        toolbar.title = CommonUtils.applyTypeface(this, movie.title ?: "", FontUtils.MEDIUM)
        collapsingLayout.title = toolbar.title
    }

    private fun setPoster(it: MovieItem) {
        val placeholderDrawable = Drawable(ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimaryDark)))
        posterImg.loadImage(PosterImage(it.posterPath),
                            placeholderDrawable = placeholderDrawable,
                            errorDrawable = placeholderDrawable,
                            diskStrategy = RESULT,
                            animate = true)
    }

    override fun setDetail(detail: DetailItem) {
        genreTxt.text = detail.genresCollect
        ratingTxt.text = getString(R.string.detail_imdb_rating, detail.imdbRating)
        expandLayout.text = detail.overview
        dateTxt.text = getString(R.string.detail_release_date, detail.releaseDate)
        tmdbRatingTxt.text = getString(R.string.detail_tmdb_rating, detail.tmdbRating)

        if (CollectionUtils.isEmpty(detail.videos?.results)) {
            trailersHeader setGone true
            trailersList setGone true
        } else {
            trailersHeader.setText(R.string.detail_trailers_header)
        }
    }

    override fun toggleFavorite(isFavorite: Boolean) {
        val drawable = AnimatedVectorDrawableCompat
                .create(this, if (isFavorite) R.drawable.avd_plus_to_cross else R.drawable.avd_cross_to_plus)

        drawable?.mutate()
        favoriteBtn.setImageDrawable(drawable)

        if (favoriteBtn.alpha != 1f) {
            favoriteBtn.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(AnimUtils.FAST_DURATION.toLong()).start()
        }

        drawable?.start()
        favoriteBtn.isEnabled = true
    }

    override fun onBackPressed() {
        favoriteBtn setGone true
        presenter?.onBackPressed(this)
        super.onBackPressed()
    }


}
