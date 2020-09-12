package com.example.mymovieapp.widget.movie

import android.content.Intent
import android.widget.RemoteViewsService

class MovieWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return MovieRemoteViewsFactory(this.applicationContext)
    }

}