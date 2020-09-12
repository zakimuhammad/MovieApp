package com.example.mymovieapp.ui.favorite.viewmodel.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mymovieapp.database.persistance.tvshow.TvShowDbDAO

/**
 * Factory for ViewModels
 */
class TvShowViewModelFactory(private val dataSource: TvShowDbDAO) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavTvShowViewModel::class.java)) {
            return FavTvShowViewModel(
                dataSource
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}