package com.artemkopan.movie.injection.module

import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import co.inteza.all.dialog.DialogProvider
import com.artemkopan.movie.injection.qualifer.ActivityContext
import com.artemkopan.movie.injection.scope.PresentationScope
import com.artemkopan.mvp.presentation.Presentation
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable


@Module
@PresentationScope
class PresentationModule(private val presentation: Presentation) {

    @Provides
    @PresentationScope
    internal fun provideDialog(): DialogProvider = DialogProvider()

    @Provides
    @ActivityContext
    @PresentationScope
    internal fun provideActivity(): FragmentActivity = presentation.baseActivity

    @Provides
    @PresentationScope
    internal fun provideFragmentManager(): FragmentManager = presentation.supportFragmentManager

    @Provides
    @PresentationScope
    internal fun provideOnDestroyDisposable(): CompositeDisposable = presentation.onDestroyDisposable

}