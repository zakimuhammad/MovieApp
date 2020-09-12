package com.example.mymovieapp.database.persistance.tvshow

import android.content.Context
import com.example.mymovieapp.database.DatabaseInterface
import com.example.mymovieapp.ui.favorite.viewmodel.tv.TvShowViewModelFactory

object InjectionTv {
    private fun provideUserDataSource(context: Context): TvShowDbDAO {
        val database = DatabaseInterface.getInstance(
            context
        )
        return database.tvShowDao()
    }

    fun provideViewModelFactory(context: Context): TvShowViewModelFactory {
        val dataSource =
            provideUserDataSource(
                context
            )
        return TvShowViewModelFactory(
            dataSource
        )
    }
}