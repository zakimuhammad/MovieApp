package com.example.mymovieapp.database.persistance.movie

import android.content.ContentValues
import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Movies")
data class Movie(
    @PrimaryKey
    @ColumnInfo(name = "movieId")
    var id: Int?,
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

    companion object : Parceler<Movie> {
        override fun Movie.write(parcel: Parcel, flags: Int) = with(parcel) {
            writeValue(id)
            writeString(title)
            rating?.let { writeDouble(it) }
            writeString(releaseDate)
            writeString(poster)
            writeString(desc)
            writeString(backdrop)
        }

        override fun create(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        fun fromContentValues(values: ContentValues?): Movie {
            return Movie(
                values?.getAsInteger("movieId"),
                values?.getAsString("title"),
                values?.getAsDouble("rating"),
                values?.getAsString("releaseDate"),
                values?.getAsString("poster"),
                values?.getAsString("desc"),
                values?.getAsString("backdrop")
            )
        }
    }
}