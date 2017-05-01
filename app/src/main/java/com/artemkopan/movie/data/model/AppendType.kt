package com.artemkopan.movie.data.model

/**
 * Created by Artem Kopan for TheMovie
 * 01.05.2017
 */

enum class AppendType constructor(val res: String) {
    VIDEOS("videos"),
    IMAGES("images")
}


class AppendResponseBuilder {

    val builder = StringBuilder()


    fun add(value: AppendType): AppendResponseBuilder {
        if (builder.isNotEmpty()) builder.append(',')
        builder.append(value.res)
        return this
    }

    override fun toString(): String {
        return builder.toString()
    }


}