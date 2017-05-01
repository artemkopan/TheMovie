package com.artemkopan.movie.ui.detail

import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.getColor
import com.artemkopan.movie.R
import com.artemkopan.movie.R.color
import com.artemkopan.movie.data.entity.Detail
import com.artemkopan.movie.data.entity.Movie
import com.artemkopan.movie.ui.base.BaseInjectActivity
import com.artemkopan.movie.util.extensions.GlidePlaceHolder.Drawable
import com.artemkopan.movie.util.extensions.loadImage
import com.artemkopan.movie.util.glide.BlurTransformation
import com.artemkopan.movie.util.glide.PosterImage
import com.artemkopan.utils.router.ActivityBuilder
import com.artemkopan.utils.router.Router
import com.artemkopan.utils.transitions.TransitionHelper
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.layout_detail.*
import javax.inject.Inject


class DetailActivity : BaseInjectActivity<DetailPresenter, DetailView>(), DetailView {

    @Inject lateinit var trailersAdapter: TrailersAdapter

    companion object {

        private const val KEY_MOVIE = "KEY_MOVIE"

        fun route(movie: Movie): ActivityBuilder<*> {
            return Router.activity(DetailActivity::class.java).putExtra(KEY_MOVIE, movie)
        }

    }

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        TransitionHelper.waitStartEnterTransition(this)
        val movie = intent.getParcelableExtra<Movie>(KEY_MOVIE)
        if (movie == null) {
            supportFinishAfterTransition()
            return
        }
        setToolbar()
        setPoster(movie)

        TransitionHelper.onEnterSharedTransitionEndAction(this, {
            injectAsync({ it.inject(this) }, {
                presenter.loadDetail(movie.id!!)
                trailerPager.adapter = trailersAdapter
            })
        })

        genreTxt.isSelected = true
    }


    override fun onInflateLayout(): Int = com.artemkopan.movie.R.layout.activity_detail

    @Inject
    override fun injectPresenter(presenter: DetailPresenter) {
        super.injectPresenter(presenter)
    }

    override fun showProgress(tag: Any?) {

    }

    override fun hideProgress(tag: Any?) {

    }

    override fun showError(tag: Any?, error: String?) {
        super.showError(tag, error)
    }

    private fun setToolbar() {
        onToolbarInit(R.id.toolbar, R.drawable.ic_arrow_back_white_24dp)
        onToolbarSetTitle("")
        onToolbarNavigationClickListener { onBackPressed() }
    }

    private fun setPoster(it: Movie) {
        val placeholderDrawable = Drawable(ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimaryDark)))
        posterImg.loadImage(PosterImage(it.posterPath),
                            placeholderDrawable = placeholderDrawable,
                            errorDrawable = placeholderDrawable,
                            animate = true,
                            transformations = BlurTransformation(this, 10, 1, getColor(this, color.colorPrimaryDarkT80)))
    }

    override fun setDetail(detail: Detail) {
        titleTxt.text = detail.title
        genreTxt.text = detail.genresCollect
        ratingTxt.text = getString(R.string.detail_imdb_rating, 7.1f)
        expandLayout.text = detail.overview

        trailersAdapter.items = detail.videos?.results
    }
}
