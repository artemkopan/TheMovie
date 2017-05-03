package com.artemkopan.movie.injection.module

import android.content.Context
import com.artemkopan.movie.data.database.DatabaseManager
import com.artemkopan.movie.injection.qualifer.ApplicationContext

import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module()
class ApplicationModule(val context: Context) {


    @Provides
    @Singleton
    @ApplicationContext
    internal fun provideApplicationContext(): Context = context



}