package com.example.mymovieapp.model.tvshow

import com.google.gson.annotations.SerializedName

data class TVShowModel(
    @SerializedName("first_air_date")
    val firstAirDate: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("original_name")
    val originalName: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("backdrop_path")
    var backdropPath: String? = null
)