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
import com.example.favorite.adapter.TvAdapter
import com.example.favorite.helper.FavoriteCursorHelper
import com.example.favorite.model.TvShow
import kotlinx.android.synthetic.main.fragment_tv_show.*

class TvShowFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private val tvshow: MutableList<TvShow?> = mutableListOf()
    private lateinit var adapter: TvAdapter

    companion object {
        const val AUTHORITY_TV = "com.example.mymovieapp"
        const val TABLE_TV = "tvshows"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_tvshow.setHasFixedSize(true)
        adapter = TvAdapter()

        rv_tvshow.layoutManager = LinearLayoutManager(context)
        rv_tvshow.adapter = adapter

        LoaderManager.getInstance(this).initLoader(100, null, this)
    }

    private val contentUri = Uri.Builder()
        .scheme("content")
        .authority(AUTHORITY_TV)
        .appendPath(TABLE_TV)
        .build()

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        pb_tvshow.visibility = View.VISIBLE
        return CursorLoader(this.context!!, contentUri, null, null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        tvshow.clear()
        tvshow.addAll(
            FavoriteCursorHelper.favCursorToListTv(
                data
            )
        )
        adapter.setData(tvshow)
        adapter.notifyDataSetChanged()
        pb_tvshow.visibility = View.GONE
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        tvshow.clear()
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        LoaderManager.getInstance(this).restartLoader(100, null, this)
    }
}
