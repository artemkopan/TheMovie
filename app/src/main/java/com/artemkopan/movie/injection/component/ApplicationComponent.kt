package com.artemkopan.movie.injection.component

import com.artemkopan.movie.injection.module.internal.NetworkModule
import com.artemkopan.movie.injection.module.internal.UtilModule
import com.artemkopan.movie.injection.module.ApplicationModule
import com.artemkopan.movie.injection.module.internal.DbModule
import com.artemkopan.movie.injection.module.internal.RetrofitModule
import com.google.gson.Gson
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(ApplicationModule::class, NetworkModule::class, RetrofitModule::class, UtilModule::class,
                             DbModule::class))
interface ApplicationComponent {


    fun presentationComponentBuilder(): PresentationComponent.Builder

    //==============================================================================================
    // Network Modules
    //==============================================================================================
    //region methods

    fun provideGson(): Gson

    fun provideHttpCliente(): OkHttpClient

    fun provideRetrofi(): Retrofit

    //endregion


    //==============================================================================================
    // Utils Modules
    //==============================================================================================
    //region methods


    //endregion
}
