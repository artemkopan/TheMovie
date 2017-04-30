package com.artemkopan.movie.injection.module.internal

import android.content.Context
import android.content.res.Resources
import com.artemkopan.movie.injection.qualifer.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class UtilModule {

    @Singleton
    @Provides
    fun provideRes(@ApplicationContext context: Context): Resources = context.resources

}