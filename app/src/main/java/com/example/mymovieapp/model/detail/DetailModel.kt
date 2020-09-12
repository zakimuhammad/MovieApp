package com.example.mymovieapp.model.detail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DetailMovieModel(

    @SerializedName("poster_path")
    @Expose
    var posterPath: String? = null,
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("title")
    @Expose
    var title: String? = null,
    @SerializedName("vote_average")
    @Expose
    var voteAverage: Double? = null,
    @SerializedName("overview")
    @Expose
    var overview: String? = null,
    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null,
    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String? = null
)

data class DetailTVShowModel(
    @SerializedName("first_air_date")
    @Expose
    val firstAirDate: String? = null,
    @SerializedName("id")
    @Expose
    val id: Int? = null,
    @SerializedName("original_name")
    @Expose
    val originalName: String? = null,
    @SerializedName("overview")
    @Expose
    val overview: String? = null,
    @SerializedName("poster_path")
    @Expose
    val posterPath: String? = null,
    @SerializedName("vote_average")
    @Expose
    val voteAverage: Double? = null,
    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String? = null
)