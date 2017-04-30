package com.artemkopan.movie.injection.module.internal

import com.artemkopan.movie.BuildConfig
import com.artemkopan.movie.data.model.movie.MovieService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class RetrofitModule {


    @Provides
    @Singleton
    internal fun provideRetrofit(gson: Gson, httpClient: OkHttpClient): Retrofit {
        return Builder()
                .baseUrl(BuildConfig.ENDPOINT)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .build()
    }


    @Provides
    @Singleton
    internal fun provideMovieService(retrofit: Retrofit): MovieService {
        return retrofit.create(MovieService::class.java)
    }

}
