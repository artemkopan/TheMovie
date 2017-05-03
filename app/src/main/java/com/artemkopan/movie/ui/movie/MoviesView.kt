package com.artemkopan.movie.ui.movie

import android.support.annotation.StringRes
import com.artemkopan.mvp.view.BaseView


interface MoviesView : BaseView {
    fun updateTitle(@StringRes stringRes: Int)
    fun showEmptyList(empty: Boolean)
}
