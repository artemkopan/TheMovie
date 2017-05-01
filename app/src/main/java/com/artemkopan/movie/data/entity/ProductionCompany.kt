package com.artemkopan.movie.data.entity

import com.google.gson.annotations.SerializedName

data class ProductionCompany(

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("id")
        val id: Int? = null
)