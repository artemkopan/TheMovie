package com.artemkopan.movie.util

import android.os.Parcel
import android.os.Parcelable


/**
 * Created by Artem Kopan for TheMovie
 * 30.04.2017
 */
data class Pagination(var page: Int = DEFAULT_VAL, var total: Int = DEFAULT_VAL) : Iterator<Int>, Parcelable {
    override fun hasNext() = page <= total

    override fun next(): Int {
        return ++page
    }

    fun reset() {
        page = DEFAULT_VAL
        total = DEFAULT_VAL
    }

    companion object {
        const val DEFAULT_VAL = 1

        @JvmField val CREATOR: Parcelable.Creator<Pagination> = object : Parcelable.Creator<Pagination> {
            override fun createFromParcel(source: Parcel): Pagination = Pagination(source)
            override fun newArray(size: Int): Array<Pagination?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(page)
        dest?.writeInt(total)
    }
}