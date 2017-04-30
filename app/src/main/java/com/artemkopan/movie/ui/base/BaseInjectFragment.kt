package com.artemkopan.movie.ui.base

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import co.inteza.all.dialog.DialogProvider
import com.artemkopan.movie.App
import com.artemkopan.movie.injection.component.PresentationComponent
import com.artemkopan.movie.injection.module.PresentationModule
import com.artemkopan.mvp.fragment.BaseFragment
import com.artemkopan.mvp.presenter.BasePresenter
import com.artemkopan.mvp.view.BaseView
import com.artemkopan.utils.ExtraUtils
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.AsyncSubject
import javax.inject.Inject

abstract class BaseInjectFragment<P : BasePresenter<V>, V : BaseView> : BaseFragment<P, V>() {


    @Inject @JvmField var dialogProvider: DialogProvider? = null
    protected lateinit var onViewCreated: AsyncSubject<Boolean>
    protected var _component: PresentationComponent? = null

    open fun getPresentationComponent(): PresentationComponent {
        if (_component == null) {
            _component = App[context]
                    .appComponent
                    .presentationComponentBuilder()
                    .presentationModule(PresentationModule(this))
                    .build()
        }
        return _component!!
    }

    protected inline fun injectAsync(crossinline callback: (PresentationComponent) -> Unit,
                                     crossinline complete: () -> Unit) {
        Completable
                .create { callback(getPresentationComponent()); it.onComplete() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .andThen(onViewCreated)
                .subscribe { complete.invoke() }
                .addTo(onDestroyDisposable)
    }

    @Inject
    protected fun injectPresenter(presenter: P) {
        this.presenter = presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onViewCreated = AsyncSubject.create()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        onViewCreated.onNext(true)
        onViewCreated.onComplete()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        ExtraUtils.hideKeyboard(view)
        dialogProvider?.dismissMessage()
        dialogProvider?.dismissProgress()
        super.onDestroyView()
    }

    override fun onDestroy() {
        _component = null
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            activity.onBackPressed()
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
