package com.example.mymovieapp.ui.movies

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymovieapp.database.movie.MoviesDAO
import com.example.mymovieapp.model.detail.DetailMovieModel
import com.example.mymovieapp.model.movies.MovieResponseModel
import com.example.mymovieapp.network.ApiMain
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel() {

    private var status = MutableLiveData<Boolean>()

    val listMovie = MutableLiveData<ArrayList<MoviesDAO>>()
    val detailMovie = MutableLiveData<MoviesDAO>()

    internal fun setMovie(apiKey: String, lang: String) {
        val listItems = ArrayList<MoviesDAO>()
        status.value = true

        ApiMain().services.getPopularMovie(apiKey, lang)
            .enqueue(object : Callback<MovieResponseModel> {
                override fun onResponse(
                    call: Call<MovieResponseModel>,
                    response: Response<MovieResponseModel>
                ) {
                    try {
                        status.value = false
                        if (response.isSuccessful) {
                            response.body()?.results?.let {
                                for (t in it) {
                                    val poster = t?.posterPath
                                    val movies = MoviesDAO(
                                        t?.id,
                                        t?.title,
                                        t?.voteAverage,
                                        t?.releaseDate,
                                        poster,
                                        t?.overview,
                                        t?.backdropPath
                                    )

                                    listItems.add(movies)
                                }
                            }
                            listMovie.postValue(listItems)
                        }
                    } catch (e: Exception) {
                        Log.d("Exception", e.message.toString())
                        status.value = true
                    }
                }

                override fun onFailure(call: Call<MovieResponseModel>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                    status.value = true
                }
            })
    }

    internal fun getMovie(): LiveData<ArrayList<MoviesDAO>> {
        return listMovie
    }

    internal fun setDetailMovie(id: Int, apiKey: String, lang: String) {
        ApiMain().services.loadMovieDetail(id, apiKey, lang)
            .enqueue(object : Callback<DetailMovieModel> {
                override fun onResponse(
                    call: Call<DetailMovieModel>,
                    response: Response<DetailMovieModel>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val poster = it.posterPath

                            val movies = MoviesDAO(
                                it.id,
                                it.title,
                                it.voteAverage,
                                it.releaseDate,
                                poster,
                                it.overview,
                                it.backdropPath
                            )
                            detailMovie.postValue(movies)
                        }
                    }
                }

                override fun onFailure(call: Call<DetailMovieModel>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                }
            })
    }

    internal fun getDetailMovie(): LiveData<MoviesDAO> {
        return detailMovie
    }

    internal fun getStatus(): MutableLiveData<Boolean> {
        status.value = true
        return status
    }

    internal fun searchMovie(apiKey: String, lang: String, query: String) {
        val listItems = ArrayList<MoviesDAO>()
        status.value = true

        ApiMain().services.findMovie(apiKey, lang, query)
            .enqueue(object : Callback<MovieResponseModel> {
                override fun onResponse(
                    call: Call<MovieResponseModel>,
                    response: Response<MovieResponseModel>
                ) {
                    try {
                        status.value = false
                        if (response.isSuccessful) {
                            response.body()?.results?.let {
                                for (t in it) {
                                    val poster = t?.posterPath
                                    val movies = MoviesDAO(
                                        t?.id,
                                        t?.title,
                                        t?.voteAverage,
                                        t?.releaseDate,
                                        poster,
                                        t?.overview,
                                        t?.backdropPath
                                    )
                                    listItems.add(movies)
                                }
                            }
                            listMovie.postValue(listItems)
                        }
                    } catch (e: Exception) {
                        Log.d("Exception", e.message.toString())
                        status.value = true
                    }
                }

                override fun onFailure(call: Call<MovieResponseModel>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                    status.value = true
                }
            })
    }
}