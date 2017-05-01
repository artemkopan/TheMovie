package com.artemkopan.movie.data.entity

import com.google.gson.annotations.SerializedName

data class Images(


        @field:SerializedName("posters")
        val posters: List<Poster?>? = null
)