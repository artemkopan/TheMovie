package com.artemkopan.movie.util


/**
 * Created by Artem Kopan for TheMovie
 * 30.04.2017
 */
data class Pagination(var page: Int = DEFAULT_VAL, var total: Int = DEFAULT_VAL) : Iterator<Int> {

    companion object {
        private const val DEFAULT_VAL = 1
    }

    override fun hasNext() = page <= total

    override fun next(): Int {
        return ++page
    }

    fun reset() {
        page = DEFAULT_VAL
        total = DEFAULT_VAL
    }
}