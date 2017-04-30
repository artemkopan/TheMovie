package com.artemkopan.movie.ui.base

import android.os.Bundle
import android.view.View
import com.artemkopan.movie.App
import com.artemkopan.movie.injection.component.PresentationComponent
import com.artemkopan.movie.injection.module.PresentationModule
import com.artemkopan.mvp.dialog.BaseDialogFragment
import com.artemkopan.mvp.presenter.BasePresenter
import com.artemkopan.mvp.view.BaseView
import com.artemkopan.utils.ExtraUtils
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.AsyncSubject

/**
 * Created for MediMee.
 * Author Artem Kopan.
 * Date 09.12.2016 16:53
 */
abstract class BaseInjectDialog<P : BasePresenter<V>, V : BaseView> : BaseDialogFragment<P, V>() {

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
        onDestroyDisposable.add(Completable
                                        .create { callback(getPresentationComponent()); it.onComplete() }
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .andThen(onViewCreated)
                                        .subscribe { complete.invoke() })
    }

    protected fun injectPresenter(presenter: P) {
        this.presenter = presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onViewCreated = AsyncSubject.create()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated.onNext(true)
        onViewCreated.onComplete()
    }

    override fun onDestroyView() {
        ExtraUtils.hideKeyboard(view)
        super.onDestroyView()
    }

    override fun onDestroy() {
        _component = null
        super.onDestroy()
    }

}