package com.example.mymovieapp.database.movie

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MoviesDAO(
    var id: Int?,
    var title: String?,
    var rating: Double?,
    var releaseDate: String?,
    var poster: String?,
    var desc: String?,
    var backdrop: String?
) : Parcelable