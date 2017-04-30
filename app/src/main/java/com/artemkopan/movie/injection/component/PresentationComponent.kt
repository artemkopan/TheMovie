package com.artemkopan.movie.injection.component

import com.artemkopan.movie.injection.module.PresentationModule
import com.artemkopan.movie.injection.scope.PresentationScope
import dagger.Subcomponent


@PresentationScope
@Subcomponent(modules = arrayOf(PresentationModule::class))
interface PresentationComponent {


    @Subcomponent.Builder
    interface Builder {
        fun presentationModule(module: PresentationModule): Builder

        fun build(): PresentationComponent
    }


}
