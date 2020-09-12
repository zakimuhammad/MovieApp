package com.example.mymovieapp.ui.tvshow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymovieapp.database.tv.TvShowsDAO
import com.example.mymovieapp.model.detail.DetailTVShowModel
import com.example.mymovieapp.model.tvshow.TVResponseModel
import com.example.mymovieapp.network.ApiMain
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TVViewModel : ViewModel() {

    private var status = MutableLiveData<Boolean>()

    val listTvShows = MutableLiveData<ArrayList<TvShowsDAO>>()
    val detailTvShow = MutableLiveData<TvShowsDAO>()

    internal fun setTvShow(apiKey: String, lang: String) {
        val listItems = ArrayList<TvShowsDAO>()
        status.value = true

        ApiMain().services.getPopularTVShow(apiKey, lang)
            .enqueue(object : Callback<TVResponseModel> {
                override fun onResponse(
                    call: Call<TVResponseModel>,
                    response: Response<TVResponseModel>
                ) {
                    try {
                        status.value = false
                        if (response.isSuccessful) {
                            response.body()?.results?.let {
                                for (t in it) {
                                    val poster = t.posterPath
                                    val tvItems = TvShowsDAO(
                                        t.id,
                                        t.originalName,
                                        t.voteAverage,
                                        t.firstAirDate,
                                        poster,
                                        t.overview,
                                        t.backdropPath
                                    )

                                    listItems.add(tvItems)
                                }
                            }
                            listTvShows.postValue(listItems)
                        }
                    } catch (e: Exception) {
                        Log.d("Exception", e.message.toString())
                        status.value = true
                    }
                }

                override fun onFailure(call: Call<TVResponseModel>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                    status.value = true
                }
            })
    }

    internal fun getTvShows(): LiveData<ArrayList<TvShowsDAO>> {
        return listTvShows
    }

    internal fun setDetailTvShow(id: Int, apiKey: String, lang: String) {
        ApiMain().services.loadTVShowDetail(id, apiKey, lang)
            .enqueue(object : Callback<DetailTVShowModel> {
                override fun onResponse(
                    call: Call<DetailTVShowModel>,
                    response: Response<DetailTVShowModel>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val poster = it.posterPath

                            val tvItems = TvShowsDAO(
                                it.id,
                                it.originalName,
                                it.voteAverage,
                                it.firstAirDate,
                                poster,
                                it.overview,
                                it.backdropPath
                            )
                            detailTvShow.postValue(tvItems)
                        }
                    }
                }

                override fun onFailure(call: Call<DetailTVShowModel>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                }
            })
    }

    internal fun getDetailTvShow(): LiveData<TvShowsDAO> {
        return detailTvShow
    }

    internal fun getStatus(): MutableLiveData<Boolean> {
        status.value = true
        return status
    }

    internal fun searchTv(apiKey: String, lang: String, query: String) {
        val listItems = ArrayList<TvShowsDAO>()
        status.value = true

        ApiMain().services.findTvShow(apiKey, lang, query)
            .enqueue(object : Callback<TVResponseModel> {
                override fun onResponse(
                    call: Call<TVResponseModel>,
                    response: Response<TVResponseModel>
                ) {
                    try {
                        status.value = false
                        if (response.isSuccessful) {
                            response.body()?.results?.let {
                                for (t in it) {
                                    val poster = t.posterPath
                                    val tvItems = TvShowsDAO(
                                        t.id,
                                        t.originalName,
                                        t.voteAverage,
                                        t.firstAirDate,
                                        poster,
                                        t.overview,
                                        t.backdropPath
                                    )
                                    listItems.add(tvItems)
                                }
                            }
                            listTvShows.postValue(listItems)
                        }
                    } catch (e: Exception) {
                        Log.d("Exception", e.message.toString())
                        status.value = true
                    }
                }

                override fun onFailure(call: Call<TVResponseModel>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                    status.value = true
                }
            })
    }
}