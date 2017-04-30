package com.artemkopan.movie.data.model.movie

import com.artemkopan.movie.data.entity.Movie
import com.artemkopan.movie.data.entity.Result
import com.artemkopan.movie.data.exception.handleException
import io.reactivex.Single
import retrofit2.http.Query
import javax.inject.Inject

/**
 * Created by Artem Kopan for TheMovie
 * 30.04.2017
 */

class MovieModel @Inject constructor() {

    @Inject
    internal lateinit var service: MovieService

    fun getPopular(@Query("api_key") apiKey: String, @Query("page") page: Int): Single<Result<List<Movie>>> {
        return service.getPopular(apiKey, page).handleException()
    }


    fun getTopRated(@Query("api_key") apiKey: String, @Query("page") page: Int): Single<Result<List<Movie>>> {
        return service.getTopRated(apiKey, page).handleException()
    }

}