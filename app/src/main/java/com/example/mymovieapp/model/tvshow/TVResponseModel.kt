package com.example.mymovieapp.model.tvshow


import com.google.gson.annotations.SerializedName

data class TVResponseModel(
    @SerializedName("results")
    val results: ArrayList<TVShowModel>
)