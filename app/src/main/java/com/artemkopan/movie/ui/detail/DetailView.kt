package com.artemkopan.movie.ui.detail

import com.artemkopan.movie.data.entity.Detail
import com.artemkopan.mvp.view.BaseView


interface DetailView : BaseView {

    fun setDetail(detail: Detail)

}
