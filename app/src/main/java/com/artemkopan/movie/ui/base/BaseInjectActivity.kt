package com.artemkopan.movie.ui.base

import android.view.MenuItem
import co.inteza.all.dialog.DialogProvider
import com.artemkopan.movie.App
import com.artemkopan.movie.injection.component.PresentationComponent
import com.artemkopan.movie.injection.module.PresentationModule
import com.artemkopan.mvp.activity.BaseActivity
import com.artemkopan.mvp.presenter.BasePresenter
import com.artemkopan.mvp.view.BaseView
import com.artemkopan.utils.ExtraUtils
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


abstract class BaseInjectActivity<P : BasePresenter<V>, V : BaseView> : BaseActivity<P, V>() {

    @Inject @JvmField
    var dialogProvider: DialogProvider? = null

    protected var _component: PresentationComponent? = null

    open fun getPresentationComponent(): PresentationComponent {
        if (_component == null) {
            _component = App[this]
                    .appComponent
                    .presentationComponentBuilder()
                    .presentationModule(PresentationModule(this))
                    .build()
        }
        return _component!!
    }

    protected inline fun injectAsync(crossinline callback: (PresentationComponent) -> Unit,
                                     crossinline complete: () -> Unit) {
        Completable.create { callback(getPresentationComponent()); it.onComplete() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { complete.invoke() }
                .addTo(onDestroyDisposable)
    }

    open fun injectPresenter(presenter: P) {
        super.injectPresenter(presenter, true)
    }

    override fun onDestroy() {
        ExtraUtils.hideKeyboard(currentFocus)
        dialogProvider?.let {
            it.dismissMessage()
            it.dismissProgress()
        }
        _component = null
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    override fun showError(tag: Any?, error: String?) {
        dialogProvider?.showMessage(this, error)
    }

    override fun showProgress(tag: Any?) {
        dialogProvider?.showProgress(this)
    }

    override fun hideProgress(tag: Any?) {
        dialogProvider?.dismissProgress()
    }
}

