package com.example.mymovieapp.model.movies


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieResponseModel(
    @field:SerializedName("results")
    val results: MutableList<MovieModel?>? = null
) : Parcelable