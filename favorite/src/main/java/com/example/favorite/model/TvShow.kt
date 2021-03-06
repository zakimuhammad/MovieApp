package com.example.favorite.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "TvShows")
data class TvShow(
    @PrimaryKey
    @ColumnInfo(name = "tvId")
    val id: Int?,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "rating")
    val rating: Double?,

    @ColumnInfo(name = "releaseDate")
    val releaseDate: String?,

    @ColumnInfo(name = "poster")
    val poster: String?,

    @ColumnInfo(name = "desc")
    val desc: String?,

    @ColumnInfo(name = "backdrop")
    val backdrop: String?
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readString(),
        source.readDouble(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()
    )

    companion object : Parceler<TvShow> {
        override fun TvShow.write(parcel: Parcel, flags: Int) = with(parcel) {
            writeValue(id)
            writeString(title)
            rating?.let { writeDouble(it) }
            writeString(releaseDate)
            writeString(poster)
            writeString(desc)
            writeString(backdrop)
        }

        override fun create(parcel: Parcel): TvShow {
            return TvShow(parcel)
        }
    }
}