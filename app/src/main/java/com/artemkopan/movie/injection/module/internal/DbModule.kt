package com.artemkopan.movie.injection.module.internal

import com.artemkopan.movie.data.database.DatabaseManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {


    @Provides
    @Singleton
    internal fun provideDatabaseManager(): DatabaseManager = DatabaseManager()

}