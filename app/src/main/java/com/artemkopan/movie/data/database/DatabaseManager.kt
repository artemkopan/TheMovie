package com.artemkopan.movie.data.database

import com.artemkopan.movie.data.entity.MovieItem
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmQuery
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean


class DatabaseManager {


    companion object {
        private val EXECUTOR = Executors.newSingleThreadExecutor()
    }


    fun findMovies(id: Long): Single<List<MovieItem>> {
        return findItems(MovieItem::class.java, { it.equalTo("id", id) })
    }

    //==============================================================================================
    // Basic methods
    //==============================================================================================
    //region methods

    fun <T : RealmModel> createOrUpdate(items: List<T>): Single<List<T>> {
        return Single.create<List<T>> { e ->
            val realm = Realm.getDefaultInstance()
            e.setDisposable(RealmDisposable(realm))
            realm.beginTransaction()
            realm.copyToRealmOrUpdate(items)
            realm.commitTransaction()
            realm.close()
            e.onSuccess(items)

        }
    }

    fun <T : RealmModel> createOrUpdate(item: T): Single<T> {
        return Single.create<T> { e ->
            val realm = Realm.getDefaultInstance()
            e.setDisposable(RealmDisposable(realm))
            realm.beginTransaction()
            realm.copyToRealmOrUpdate(item)
            realm.commitTransaction()
            realm.close()
            e.onSuccess(item)

        }
    }

    inline fun <T : RealmModel> findItems(clazz: Class<T>,
                                          crossinline runnableQuery: (RealmQuery<T>) -> Unit): Single<List<T>> {
        return Single.create<List<T>> { e ->
            val realm = Realm.getDefaultInstance()
            e.setDisposable(RealmDisposable(realm))

            val query = realm.where(clazz)

            runnableQuery(query)

            e.onSuccess(realm.copyFromRealm(query.findAll()))
            realm.close()

        }
    }

    inline fun <T : RealmModel> removeItems(clazz: Class<T>, crossinline runnableQuery: (RealmQuery<T>) -> Unit): Single<Boolean> {
        return Single.create<Boolean> { e ->
            val realm = Realm.getDefaultInstance()
            e.setDisposable(RealmDisposable(realm))

            realm.beginTransaction()

            val query = realm.where<T>(clazz)

            runnableQuery(query)

            val result = query.findAll()
            result.deleteAllFromRealm()
            realm.commitTransaction()
            realm.close()
            e.onSuccess(true)
        }
    }


    inner class RealmDisposable constructor(private val realm: Realm?) : Disposable {

        private val unsubscribed = AtomicBoolean()

        override fun dispose() {
            if (unsubscribed.compareAndSet(false, true)) {
                Schedulers.from(EXECUTOR).scheduleDirect {
                    if (realm != null && !realm.isClosed) {
                        realm.close()
                    }
                }
            }
        }

        override fun isDisposed(): Boolean {
            return unsubscribed.get()
        }
    }


    //endregion
}
