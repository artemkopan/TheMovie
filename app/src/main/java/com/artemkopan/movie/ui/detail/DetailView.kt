package com.artemkopan.movie.ui.detail

import com.artemkopan.movie.data.entity.DetailItem
import com.artemkopan.mvp.view.BaseView


interface DetailView : BaseView {

    fun setDetail(detail: DetailItem)

    fun toggleFavorite(isFavorite: Boolean)
}
