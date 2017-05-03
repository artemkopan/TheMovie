package com.artemkopan.movie.data.entity

import com.google.gson.annotations.SerializedName


data class ImdbRating(

        @field:SerializedName("imdbRating")
        val imdbRating: String? = null
)