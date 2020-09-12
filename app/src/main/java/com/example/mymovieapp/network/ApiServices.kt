package com.example.mymovieapp.network

import com.example.mymovieapp.model.detail.DetailMovieModel
import com.example.mymovieapp.model.detail.DetailTVShowModel
import com.example.mymovieapp.model.movies.MovieResponseModel
import com.example.mymovieapp.model.tvshow.TVResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    @GET("discover/movie")
    fun getPopularMovie(
        @Query("api_key") api: String,
        @Query("language") language: String = "en-US"
    ): Call<MovieResponseModel>

    @GET("discover/tv")
    fun getPopularTVShow(
        @Query("api_key") api: String,
        @Query("language") language: String = "en-US"
    ): Call<TVResponseModel>

    @GET("movie/{id}")
    fun loadMovieDetail(
        @Path("id") id: Int,
        @Query("api_key") api_key: String,
        @Query("language") language: String = "en-US"
    ): Call<DetailMovieModel>

    @GET("tv/{id}")
    fun loadTVShowDetail(
        @Path("id") id: Int,
        @Query("api_key") api_key: String,
        @Query("language") language: String = "en-US"
    ): Call<DetailTVShowModel>

    @GET("search/movie")
    fun findMovie(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("query") query: String
    ): Call<MovieResponseModel>

    @GET("search/tv")
    fun findTvShow(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("query") query: String
    ): Call<TVResponseModel>

    @GET("discover/movie")
    fun getReleaseMovie(
        @Query("api_key") apiKei: String,
        @Query("primary_release_date.gte") primaryReleaseDateGte: String,
        @Query("primary_release_date.lte") primaryReleaseDateLte: String
    ): Call<MovieResponseModel>
}