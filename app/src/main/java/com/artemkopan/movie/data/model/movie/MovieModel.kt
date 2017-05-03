package com.artemkopan.movie.data.model.movie

import com.artemkopan.movie.data.entity.DetailItem
import com.artemkopan.movie.data.entity.MovieItem
import com.artemkopan.movie.data.entity.MovieResponse
import com.artemkopan.movie.data.exception.handleException
import com.artemkopan.movie.data.model.AppendResponseBuilder
import com.artemkopan.utils.CollectionUtils
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Artem Kopan for TheMovie
 * 30.04.2017
 */

class MovieModel @Inject constructor() {

    @Inject
    internal lateinit var service: MovieService

    fun getPopular(apiKey: String, page: Int): Single<MovieResponse<List<MovieItem>>> {
        return service.getPopular(apiKey, page).handleException()
    }


    fun getTopRated(apiKey: String, page: Int): Single<MovieResponse<List<MovieItem>>> {
        return service.getTopRated(apiKey, page).handleException()
    }

    fun getMovieDetail(apiKey: String, id: Long, append: AppendResponseBuilder, genreSep: Char = '|'): Single<DetailItem> {
        return service.getMovieDetail(id, apiKey, append)
                .flatMap { detail ->
                    if (detail.imdbId.isNullOrEmpty()) {
                        return@flatMap Single.just(detail)
                    } else {
                        // load imdb rating by id
                        return@flatMap service.getImdbRating(detail.imdbId!!)
                                .map {
                                    // update rating and return detail item
                                    it.imdbRating?.let { detail.imdbRating = it }
                                    return@map detail
                                }
                                .onErrorResumeNext { _ ->
                                    return@onErrorResumeNext Single.just(detail)
                                }
                    }
                }
                .map {
                    // Concat all genres in one string. Example "Action | Comedy | Horror"
                    if (!CollectionUtils.isEmpty(it.genres)) {
                        val builder = StringBuilder()
                        val size = it.genres!!.size
                        it.genres.map { it.name }
                                .forEachIndexed { index, s ->
                                    builder.append(s)
                                    if (index != size - 1) {
                                        builder.append(' ')
                                        builder.append(genreSep)
                                        builder.append(' ')
                                    }
                                }
                        it.genresCollect = builder.toString()
                    }
                    return@map it
                }
                .handleException()
    }


}