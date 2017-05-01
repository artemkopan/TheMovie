package com.artemkopan.movie.injection.component

import com.artemkopan.movie.injection.builder.SubComponentBuilder
import com.artemkopan.movie.injection.module.PresentationModule
import com.artemkopan.movie.injection.scope.PresentationScope
import com.artemkopan.movie.ui.detail.DetailActivity
import com.artemkopan.movie.ui.movie.MoviesActivity
import dagger.Subcomponent


@PresentationScope
@Subcomponent(modules = arrayOf(PresentationModule::class))
interface PresentationComponent {

    @Subcomponent.Builder
    interface Builder : SubComponentBuilder<PresentationComponent> {
        fun presentationModule(module: PresentationModule): Builder
    }

    fun inject(moviesActivity: MoviesActivity)
    fun inject(moviesActivity: DetailActivity)

}
