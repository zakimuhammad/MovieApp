package com.example.mymovieapp.widget.movie

import android.content.Context
import com.example.mymovieapp.database.DatabaseInterface
import com.example.mymovieapp.database.persistance.movie.Movie

class MovieWidgetPresenter(var context: Context) {
    private val database = DatabaseInterface.getInstance(context)

    fun getListFavoriteMovies(): List<Movie> {
        return database.movieDao().all
    }
}