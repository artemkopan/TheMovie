package com.artemkopan.movie

import android.app.Application
import android.content.Context
import android.graphics.Typeface
import com.artemkopan.movie.injection.DaggerComponentProvider
import com.artemkopan.movie.util.glide.models.ApiImageModel
import com.artemkopan.movie.util.glide.models.ApiImageModelFactory
import com.artemkopan.widget.fonts.FontUtils
import com.bumptech.glide.Glide
import com.squareup.leakcanary.LeakCanary
import io.realm.Realm
import timber.log.Timber
import java.io.InputStream
import io.realm.RealmConfiguration



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

        Glide.get(this).register(ApiImageModel::class.java, InputStream::class.java, ApiImageModelFactory())

        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(realmConfiguration)

        FontUtils.addFont(Typeface.BOLD, "OpenSans-Bold.ttf")
        FontUtils.addFont(Typeface.ITALIC, "OpenSans-Italic.ttf")
        FontUtils.addFont(Typeface.NORMAL, "OpenSans-Regular.ttf")
        FontUtils.addFont(FontUtils.MEDIUM, "OpenSans-Semibold.ttf")
        FontUtils.addFont(FontUtils.LIGHT, "OpenSans-Light.ttf")
    }

}