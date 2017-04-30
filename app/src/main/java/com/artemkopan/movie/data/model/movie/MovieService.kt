package com.artemkopan.movie.data.model.movie

import com.artemkopan.movie.data.entity.Movie
import com.artemkopan.movie.data.entity.Result
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

/**
 * Created by Artem Kopan for TheMovie
 * 30.04.2017
 */
interface MovieService {

    @GET("movie/popular")
    fun getPopular(@Query("api_key") apiKey: String,
                   @Query("page") page: Int,
                   @Query("language") language: String = Locale.getDefault().language): Single<Result<List<Movie>>>


    @GET("movie/top_rated")
    fun getTopRated(@Query("api_key") apiKey: String,
                    @Query("page") page: Int,
                    @Query("language") language: String = Locale.getDefault().language): Single<Result<List<Movie>>>

}