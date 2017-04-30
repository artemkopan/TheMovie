package com.artemkopan.movie

import android.app.Application
import android.content.Context
import com.artemkopan.movie.injection.DaggerComponentProvider
import com.artemkopan.movie.util.glide.ApiImageModel
import com.artemkopan.movie.util.glide.PosterImageModelFactory
import com.artemkopan.movie.util.glide.ApiImageUrlLoader
import com.bumptech.glide.Glide
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber
import java.io.InputStream

/**
 * Created by Artem Kopan for TheMovie
 * 30.04.2017
 */
class App : Application() {

    private val componentProvider: DaggerComponentProvider = DaggerComponentProvider(this)

    companion object {

        operator fun get(context: Context): DaggerComponentProvider {
            return (context.applicationContext as App).componentProvider
        }

    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            LeakCanary.install(this)
        }

        Glide.get(this).register(ApiImageModel::class.java, InputStream::class.java, PosterImageModelFactory())

//        FontUtils.addFont(Typeface.BOLD, "Roboto-Bold.ttf")
//        FontUtils.addFont(Typeface.ITALIC, "Roboto-Italic.ttf")
//        FontUtils.addFont(Typeface.NORMAL, "Roboto-Regular.ttf")
//        FontUtils.addFont(FontUtils.MEDIUM, "Roboto-Medium.ttf")
//        FontUtils.addFont(FontUtils.LIGHT, "Roboto-Light.ttf")
    }

}