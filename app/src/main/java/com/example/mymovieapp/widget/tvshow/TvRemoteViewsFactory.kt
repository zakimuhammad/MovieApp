package com.example.mymovieapp.widget.tvshow

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.mymovieapp.R
import com.example.mymovieapp.database.persistance.tvshow.TvShow
import com.squareup.picasso.Picasso

class TvRemoteViewsFactory(val context: Context) : RemoteViewsService.RemoteViewsFactory {
    private var mWidgetItem: MutableList<TvShow> = mutableListOf()
    private lateinit var presenter: TvWidgetPresenter

    override fun onCreate() {
        presenter =
            TvWidgetPresenter(context)
        mWidgetItem.addAll(presenter.getListFavoriteMovies())
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun onDataSetChanged() {
        mWidgetItem.clear()
        mWidgetItem.addAll(presenter.getListFavoriteMovies())
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getViewAt(position: Int): RemoteViews {
        val tvShow = mWidgetItem[position]
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_item)
        val bitmap = Picasso.get().load("https://image.tmdb.org/t/p/w500" + tvShow.backdrop).get()
        remoteViews.setImageViewBitmap(R.id.imageView, bitmap)
        remoteViews.setTextViewText(R.id.movieTitle, tvShow.title)
        val extras = Bundle()
        extras.putInt(TvWidget.EXTRA_ITEM, position)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        return remoteViews
    }

    override fun getCount(): Int {
        return mWidgetItem.count()
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun onDestroy() {
    }

}