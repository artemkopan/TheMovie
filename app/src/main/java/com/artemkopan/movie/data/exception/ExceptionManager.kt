package com.artemkopan.movie.data.exception

import com.artemkopan.movie.Constants.Field
import com.artemkopan.mvp.view.BaseView
import com.artemkopan.utils.ExtraUtils
import com.google.gson.JsonParser
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import retrofit2.HttpException
import timber.log.Timber
import java.lang.Exception
import java.util.concurrent.CancellationException

object ExceptionManager {

    inline fun consumerThrowable(crossinline callback: () -> BaseView?,
                                 errorTag: Any? = null): Consumer<Throwable> {
        return Consumer {
            Timber.e(it)
            callback()?.showError(errorTag, it.message) ?: mvpNull()
        }
    }


    fun handleApiException(ex: Throwable): Throwable {
        ExtraUtils.checkBackgroundThread()
        try {
            if (ex is HttpException) {
                val response = ex.response().errorBody().string()

                val jsonElement = JsonParser().parse(response)

                if (jsonElement.isJsonObject) {
                    val jsonObj = jsonElement.asJsonObject

                    if (jsonObj.has(Field.STATUS_MESSAGE)) {
                        return ApiException(jsonObj.get(Field.STATUS_MESSAGE).asString)
                    }
                }

                return ApiException(ex.message())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ex
    }


    fun mvpNull() {
        Timber.w("MvpView is null")
    }


    class ApiException(message: String) : Exception(message)

}

fun <T> Single<T>.handleException(): Single<T> {
    return this.onErrorResumeNext(Function {
        return@Function if (it is CancellationException) {
            Single.never()
        } else {
            Single.error(ExceptionManager.handleApiException(it))
        }
    })
}

fun Completable.handleException(): Completable {
    return this.onErrorResumeNext(Function {
        return@Function if (it is CancellationException) {
            Completable.never()
        } else {
            Completable.error(ExceptionManager.handleApiException(it))
        }
    })
}
