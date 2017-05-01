package com.artemkopan.movie.data.entity

import com.google.gson.annotations.SerializedName

data class ProductionCountry(

        @field:SerializedName("iso_3166_1")
        val iso: String? = null,

        @field:SerializedName("name")
        val name: String? = null

)