package com.example.mymovieapp.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.mymovieapp.R
import com.example.mymovieapp.database.DatabaseInterface
import com.example.mymovieapp.database.persistance.movie.Movie
import com.example.mymovieapp.database.persistance.tvshow.TvShow

class Provider : ContentProvider() {

    private val authorityMovie = "com.example.mymovieapp"
    private val authorityTV = "com.example.mymovieapp"

    private val movies = 1
    private val movieItem = 2
    private val tvShows = 11
    private val tvShowItem = 12

    private lateinit var database: DatabaseInterface
    private val matcher = UriMatcher(UriMatcher.NO_MATCH)

    private val uriMatcher = matcher.apply {
        addURI(authorityMovie, "movies", movies)
        addURI(authorityMovie, "movies/#", movieItem)
        addURI(authorityTV, "tvshows", tvShows)
        addURI(authorityTV, "tvshows/#", tvShowItem)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when (matcher.match(uri)) {
            movies -> throw IllegalArgumentException("Invalid URI, can't update without ID: $uri")
            tvShows -> throw java.lang.IllegalArgumentException("Invalid URI, can't update without ID: $uri")
            movieItem -> {
                if (context == null) return 0
                val count = database.movieDao().deleteById(ContentUris.parseId(uri))
                context?.contentResolver?.notifyChange(uri, null)
                return count
            }
            tvShowItem -> {
                if (context == null) return 0
                val count = database.tvShowDao().deleteById(ContentUris.parseId(uri))
                context?.contentResolver?.notifyChange(uri, null)
                return count
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        return when (matcher.match(uri)) {
            movies -> "vnd.android.cursor.dir/$authorityMovie.movies"
            movieItem -> "vnd.android.cursor.item/$authorityMovie.movies"
            tvShows -> "vnd.android.cursor.dir/$authorityTV.tvshows"
            tvShowItem -> "vnd.android.cursor.item/$authorityTV.tvshows"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        when (matcher.match(uri)) {
            movies -> {
                if (context == null) {
                    return null
                }
                val id = database.movieDao().insert(Movie.fromContentValues(values))
                context?.contentResolver?.notifyChange(uri, null)
                return ContentUris.withAppendedId(uri, id)
            }

            tvShows -> {
                if (context == null) {
                    return null
                }
                val id = database.tvShowDao().insert(TvShow.fromContentValues(values))
                context?.contentResolver?.notifyChange(uri, null)
                return ContentUris.withAppendedId(uri, id)
            }

            movieItem -> throw IllegalArgumentException("Invalid URI, cannot insert with ID: $uri")

            tvShowItem -> throw IllegalArgumentException("Invalid URI, cannot insert with ID: $uri")

            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun onCreate(): Boolean {
        database = DatabaseInterface.getInstance(context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        if (context == null) {
            return null
        }
        val cursor: Cursor?
        val favMovie = database.movieDao()
        val favTv = database.tvShowDao()
        when (matcher.match(uri)) {
            in movies..movieItem -> {
                cursor = when (uriMatcher.match(uri)) {
                    movies -> favMovie.allFavorite()
                    movieItem -> favMovie.getFavoriteById(ContentUris.parseId(uri))
                    else -> {
                        null
                    }
                }
                cursor?.setNotificationUri(context?.contentResolver, uri)
                return cursor
            }
            in tvShows..tvShowItem -> {
                cursor = when (uriMatcher.match(uri)) {
                    tvShows -> favTv.allFavorite()
                    tvShowItem -> favTv.getFavoriteById(ContentUris.parseId(uri))
                    else -> {
                        null
                    }
                }
                cursor?.setNotificationUri(context?.contentResolver, uri)
                return cursor
            }
            else -> {
                throw IllegalArgumentException(context?.getString(R.string.unknown_uri, uri))
            }
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        when (matcher.match(uri)) {
            movies -> throw IllegalArgumentException("Invalid URI, cannot update without ID: $uri")
            movieItem -> {
                if (context == null) return 0
                val movie = Movie.fromContentValues(values)
                movie.id = ContentUris.parseId(uri).toInt()
                val count = database.movieDao().update(movie)
                context?.contentResolver?.notifyChange(uri, null)
                return count
            }
            tvShows -> throw IllegalArgumentException("Invalid URI, cannot update without ID: $uri")
            tvShowItem -> {
                if (context == null) return 0
                val tvShow = TvShow.fromContentValues(values)
                tvShow.id = ContentUris.parseId(uri).toInt()
                val count = database.tvShowDao().update(tvShow)
                context?.contentResolver?.notifyChange(uri, null)
                return count
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }
}
