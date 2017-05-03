package com.artemkopan.movie.ui.movie

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.artemkopan.movie.R
import com.artemkopan.movie.ui.base.BaseInjectActivity
import com.artemkopan.movie.util.CommonUtils
import kotlinx.android.synthetic.main.activity_movies.*
import javax.inject.Inject

class MoviesActivity : BaseInjectActivity<MoviesPresenter, MoviesView>(), MoviesView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onToolbarInit(R.id.toolbar)
        injectAsync({ it.inject(this) },
                    {
                        presenter.setList(list)
                        presenter.setType(MoviesPresenter.POPULAR)
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

    override fun updateTitle(stringRes: Int) {
        onToolbarSetTitle(CommonUtils.applyTypeface(this, stringRes))
    }

    override fun showEmptyList(empty: Boolean) {
        if (empty) {
            list.showText(R.string.base_info_items_not_found)
        } else {
            list.hideText()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_popular -> {
                presenter.setType(MoviesPresenter.POPULAR); return true
            }
            R.id.action_top_rated -> {
                presenter.setType(MoviesPresenter.TOP_RATED); return true
            }
            R.id.action_favorite -> {
                presenter.setType(MoviesPresenter.FAVORITE); return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.onActivityResult(requestCode, resultCode, data)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            (list.layoutManager as GridLayoutManager).spanCount = 3
        }else{
            (list.layoutManager as GridLayoutManager).spanCount = 2
        }
    }

}
