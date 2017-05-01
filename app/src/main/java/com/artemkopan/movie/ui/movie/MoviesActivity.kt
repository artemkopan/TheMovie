package com.artemkopan.movie.ui.movie

import android.os.Bundle
import com.artemkopan.movie.R
import com.artemkopan.movie.ui.base.BaseInjectActivity
import kotlinx.android.synthetic.main.activity_movies.*
import javax.inject.Inject

class MoviesActivity : BaseInjectActivity<MoviesPresenter, MoviesView>(), MoviesView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectAsync({ it.inject(this) },
                    {
                        presenter.setList(list)
                    })
    }

    override fun onInflateLayout(): Int = R.layout.activity_movies

    @Inject
    override fun injectPresenter(presenter: MoviesPresenter) {
        super.injectPresenter(presenter)
    }

    override fun showProgress(tag: Any?) {
        list.showProgress()
    }

    override fun hideProgress(tag: Any?) {
        list.hideProgress()
    }

    override fun showError(tag: Any?, error: String?) {
        list.showText(error)
    }

}
