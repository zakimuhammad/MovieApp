package com.example.favorite.ui

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favorite.R
import com.example.favorite.adapter.MovieAdapter
import com.example.favorite.helper.FavoriteCursorHelper
import com.example.favorite.model.Movie
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private val movie: MutableList<Movie?> = mutableListOf()
    private lateinit var adapter: MovieAdapter

    companion object {
        const val AUTHORITY_MOVIE = "com.example.mymovieapp"
        const val TABLE_MOVIE = "movies"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_movies.setHasFixedSize(true)
        adapter = MovieAdapter()

        rv_movies.layoutManager = LinearLayoutManager(context)
        rv_movies.adapter = adapter

        LoaderManager.getInstance(this).initLoader(100, null, this)
    }

    private val contentUri = Uri.Builder()
        .scheme("content")
        .authority(AUTHORITY_MOVIE)
        .appendPath(TABLE_MOVIE)
        .build()

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        pb_movies.visibility = View.VISIBLE
        return CursorLoader(this.context!!, contentUri, null, null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        movie.clear()
        movie.addAll(
            FavoriteCursorHelper.favCursorToListMovie(
                data
            )
        )
        adapter.setData(movie)
        adapter.notifyDataSetChanged()
        pb_movies.visibility = View.GONE
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        movie.clear()
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        LoaderManager.getInstance(this).restartLoader(100, null, this)
    }
}
