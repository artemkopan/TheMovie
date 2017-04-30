package com.artemkopan.movie.data.entity

import com.google.gson.annotations.SerializedName

data class Result<out T>(

        @field:SerializedName("results")
        val results: T? = null,

        @field:SerializedName("page")
        val page: Int? = null,

        @field:SerializedName("total_pages")
        val totalPages: Int? = null,

        @field:SerializedName("total_results")
        val totalResults: Int? = null

)