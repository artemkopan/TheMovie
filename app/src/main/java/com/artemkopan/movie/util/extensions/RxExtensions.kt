package com.artemkopan.movie.util.extensions

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit.MILLISECONDS


private const val DEFAULT_DELAY = 400L

fun <T> Single<T>.applyDelay(delay: Long = DEFAULT_DELAY): Single<T> {
    return zipWith(Single.timer(delay, MILLISECONDS, AndroidSchedulers.mainThread()), BiFunction { t1, _ -> t1 })
}




//==============================================================================================
// Schedulers
//==============================================================================================
//region methods

fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
}

fun <T> Maybe<T>.applySchedulers(): Maybe<T> {
    return observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
}

fun <T> Single<T>.applySchedulers(): Single<T> {
    return observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
}

fun Completable.applySchedulers(): Completable {
    return observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
}

//endregion
