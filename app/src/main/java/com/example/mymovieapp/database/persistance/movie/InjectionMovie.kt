package com.example.mymovieapp.database.persistance.movie

import android.content.Context
import com.example.mymovieapp.database.DatabaseInterface
import com.example.mymovieapp.ui.favorite.viewmodel.movie.FavMovieViewModelFactory

object InjectionMovie {
    private fun provideUserDataSource(context: Context): MovieDbDAO {
        val database = DatabaseInterface.getInstance(
            context
        )
        return database.movieDao()
    }

    fun provideViewModelFactory(context: Context): FavMovieViewModelFactory {
        val dataSource = provideUserDataSource(context)
        return FavMovieViewModelFactory(dataSource)
    }
}