package com.artemkopan.movie.ui.discover

import android.os.Bundle
import com.artemkopan.movie.R
import com.artemkopan.mvp.activity.BaseActivity

class DiscoverActivity : BaseActivity<DiscoverPresenter, DiscoverView>(), DiscoverView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onInflateLayout(): Int = R.layout.activity_discover

    override fun showProgress(tag: Any?) {
        super.showProgress(tag)
    }

    override fun hideProgress(tag: Any?) {
        super.hideProgress(tag)
    }

    override fun showError(tag: Any?, error: String?) {
        super.showError(tag, error)
    }

}
