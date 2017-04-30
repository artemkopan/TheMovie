package com.artemkopan.movie.injection

import com.artemkopan.movie.injection.module.internal.UtilModule
import com.artemkopan.movie.App
import com.artemkopan.movie.injection.component.ApplicationComponent
import com.artemkopan.movie.injection.component.DaggerApplicationComponent
import com.artemkopan.movie.injection.module.ApplicationModule

class DaggerComponentProvider(val app: App) {

    private var _appComponent: ApplicationComponent? = null

    val appComponent: ApplicationComponent
        get() {
            if (_appComponent == null) {
                _appComponent = DaggerApplicationComponent.builder()
                        .applicationModule(ApplicationModule(app))
                        .utilModule(UtilModule())
                        .build()
            }
            return _appComponent!!
        }
}