package com.artemkopan.movie.util

/**
 * Created by Artem Kopan for TheMovie
 * 30.04.2017
 */
data class Pagination(var page: Int = 1, var total: Int =1) {

    fun hasNextPage() = page <= total

}