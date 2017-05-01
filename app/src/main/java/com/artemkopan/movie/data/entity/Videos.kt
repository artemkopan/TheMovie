package com.artemkopan.movie.data.entity

import com.google.gson.annotations.SerializedName

data class Videos(

        @field:SerializedName("results")
        val results: List<Video>? = null
)