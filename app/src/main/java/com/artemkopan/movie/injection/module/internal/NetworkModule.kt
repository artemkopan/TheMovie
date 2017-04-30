package com.artemkopan.movie.injection.module.internal

import com.artemkopan.movie.BuildConfig
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.github.simonpercic.oklog3.OkLogInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Logger
import timber.log.Timber
import javax.inject.Singleton


@Module
class NetworkModule {

    @Module
    companion object {

        @Provides
        @Singleton
        @JvmStatic
        fun provideGson(): Gson {
            val builder = GsonBuilder()

            builder.setDateFormat("yyyy-MM-dd HH:mm")

            return builder.create()
        }

        @Provides
        @Singleton
        @JvmStatic
        fun provideHttpClient(): OkHttpClient {

            val builder = Builder()

            val loggingInterceptor = HttpLoggingInterceptor(Logger { Timber.i(it) }).apply {
                level = BODY
            }

            if (BuildConfig.DEBUG) {
                builder.addNetworkInterceptor(StethoInterceptor())
                builder.addNetworkInterceptor(OkLogInterceptor.builder()
                                                      .withRequestHeaders(true)
                                                      .withResponseCode(true)
                                                      .shortenInfoUrl(true)
                                                      .build())
            }

            builder.addInterceptor(loggingInterceptor)
            return builder.build()
        }
    }



}