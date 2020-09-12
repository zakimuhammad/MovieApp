package com.example.mymovieapp.ui.favorite.viewmodel.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mymovieapp.database.persistance.movie.MovieDbDAO

class FavMovieViewModelFactory(private val dataSource: MovieDbDAO) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavMovieViewModel::class.java)) {
            return FavMovieViewModel(
                dataSource
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}