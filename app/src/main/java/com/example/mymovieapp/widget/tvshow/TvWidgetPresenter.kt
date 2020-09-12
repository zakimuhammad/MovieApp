package com.example.mymovieapp.widget.tvshow

import android.content.Context
import com.example.mymovieapp.database.DatabaseInterface
import com.example.mymovieapp.database.persistance.tvshow.TvShow

class TvWidgetPresenter(var context: Context) {
    private val database = DatabaseInterface.getInstance(context)

    fun getListFavoriteMovies(): List<TvShow> {
        return database.tvShowDao().all
    }
}