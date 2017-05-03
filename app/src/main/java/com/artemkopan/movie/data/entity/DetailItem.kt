package com.artemkopan.movie.data.entity

import com.artemkopan.movie.Constants.DEFAULT_RATING
import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.annotations.PrimaryKey

data class DetailItem(
        @field:SerializedName("id")
        @PrimaryKey
        val id: Long,

        @SerializedName("imdb_id")
        val imdbId: String? = null,

        @field:SerializedName("poster_path")
        var posterPath: String? = null,

        @SerializedName("videos")
        val videos: Videos? = null,

        @SerializedName("images")
        val images: Images? = null,

        @SerializedName("genres")
        val genres: RealmList<GenresItem>? = null,

        @field:SerializedName("overview")
        val overview: String? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("release_date")
        val releaseDate: String? = null,

        @field:SerializedName("vote_average")
        val tmdbRating: String? = null,

        var imdbRating: String = DEFAULT_RATING,
        var genresCollect: String? = null,
        var isFavorite: Boolean = false
)