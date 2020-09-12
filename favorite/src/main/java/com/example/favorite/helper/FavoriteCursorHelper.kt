package com.example.favorite.helper

import android.database.Cursor
import com.example.favorite.model.Movie
import com.example.favorite.model.TvShow

class FavoriteCursorHelper {
    companion object {
        fun favCursorToListMovie(cursor: Cursor): List<Movie> {
            val movies = mutableListOf<Movie>()

            while (cursor.moveToNext()) {
                val movie = Movie(
                    cursor.getInt(cursor.getColumnIndexOrThrow("movieId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("rating")),
                    cursor.getString(cursor.getColumnIndexOrThrow("releaseDate")),
                    cursor.getString(cursor.getColumnIndexOrThrow("poster")),
                    cursor.getString(cursor.getColumnIndexOrThrow("desc")),
                    cursor.getString(cursor.getColumnIndexOrThrow("backdrop"))
                )
                movies.add(movie)
            }
            return movies
        }

        fun favCursorToListTv(cursor: Cursor): List<TvShow> {
            val tvShows = mutableListOf<TvShow>()
            while (cursor.moveToNext()) {
                val tvShow = TvShow(
                    cursor.getInt(cursor.getColumnIndexOrThrow("tvId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("rating")),
                    cursor.getString(cursor.getColumnIndexOrThrow("releaseDate")),
                    cursor.getString(cursor.getColumnIndexOrThrow("poster")),
                    cursor.getString(cursor.getColumnIndexOrThrow("desc")),
                    cursor.getString(cursor.getColumnIndexOrThrow("backdrop"))
                )
                tvShows.add(tvShow)
            }
            return tvShows
        }
    }
}