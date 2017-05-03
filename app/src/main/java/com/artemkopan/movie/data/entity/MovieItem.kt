package com.artemkopan.movie.data.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class MovieItem constructor() : RealmModel, Parcelable {

    @field:SerializedName("overview")
    var overview: String? = null

    @field:SerializedName("title")
    var title: String? = null

    @field:SerializedName("poster_path")
    var posterPath: String? = null

    @field:SerializedName("id")
    @PrimaryKey
    var id: Long = 0


    companion object {
        @JvmField val CREATOR: Parcelable.Creator<MovieItem> = object : Parcelable.Creator<MovieItem> {
            override fun createFromParcel(source: Parcel): MovieItem = MovieItem(source)
            override fun newArray(size: Int): Array<MovieItem?> = arrayOfNulls(size)
        }
    }

    constructor(
            overview: String,
            title: String,
            posterPath: String,
            id: Long) : this() {
        this.overview = overview
        this.title = title
        this.posterPath = posterPath
        this.id = id
    }

    constructor(detailItem: DetailItem) : this() {
        this.overview = detailItem.overview
        this.title = detailItem.title
        this.posterPath = detailItem.posterPath
        this.id = detailItem.id
    }


    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Long
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(overview)
        dest?.writeString(title)
        dest?.writeString(posterPath)
        dest?.writeValue(id)
    }
}