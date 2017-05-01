package com.artemkopan.movie.data.model.movie

import com.artemkopan.movie.data.entity.Detail
import com.artemkopan.movie.data.entity.Movie
import com.artemkopan.movie.data.entity.MovieResponse
import com.artemkopan.movie.data.model.AppendResponseBuilder
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

/**
 * Created by Artem Kopan for TheMovie
 * 30.04.2017
 */
interface MovieService {

    companion object{
        private val LANGUAGE = Locale.US.language
    }

    @GET("movie/popular")
    fun getPopular(@Query("api_key") apiKey: String,
                   @Query("page") page: Int,
                   @Query("language") language: String = LANGUAGE): Single<MovieResponse<List<Movie>>>


    @GET("movie/top_rated")
    fun getTopRated(@Query("api_key") apiKey: String,
                    @Query("page") page: Int,
                    @Query("language") language: String = LANGUAGE): Single<MovieResponse<List<Movie>>>

    @GET("movie/{id}")
    fun getMovieDetail(@Path("id") id: Int,
                       @Query("api_key") apiKey: String,
                       @Query("append_to_response") append: AppendResponseBuilder,
                       @Query("language") language: String = LANGUAGE): Single<Detail>

}