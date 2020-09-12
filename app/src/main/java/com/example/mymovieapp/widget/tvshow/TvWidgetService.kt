package com.example.mymovieapp.widget.tvshow

import android.content.Intent
import android.widget.RemoteViewsService

class TvWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return TvRemoteViewsFactory(this.applicationContext)
    }
}