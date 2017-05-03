package com.artemkopan.movie.injection.module.internal

import android.content.Context
import com.artemkopan.movie.BuildConfig
import com.artemkopan.movie.injection.qualifer.ApplicationContext
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.github.simonpercic.oklog3.OkLogInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Logger
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit
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
        fun provideHttpClient(@ApplicationContext context: Context): OkHttpClient {

            val cache = Cache(File(context.cacheDir, "http-cache"), 10 * 1024 * 1024)

            val builder = Builder()

            builder.cache(cache)

            val loggingInterceptor = HttpLoggingInterceptor(Logger { Timber.i(it) }).apply {
                level = BODY
            }

            builder.addNetworkInterceptor {
                val response = it.proceed(it.request())
                val cacheControl = CacheControl.Builder()
                        .maxAge(2, TimeUnit.MINUTES)
                        .build()
                return@addNetworkInterceptor response.newBuilder()
                        .header("Cache-Control", cacheControl.toString())
                        .build()
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