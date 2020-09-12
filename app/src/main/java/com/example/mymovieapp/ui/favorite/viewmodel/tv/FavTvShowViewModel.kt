package com.example.mymovieapp.ui.favorite.viewmodel.tv

import androidx.lifecycle.ViewModel
import com.example.mymovieapp.database.persistance.tvshow.TvShow
import com.example.mymovieapp.database.persistance.tvshow.TvShowDbDAO
import com.example.mymovieapp.database.tv.TvShowsDAO
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * View Model for the [FavMovieFragment]
 */
class FavTvShowViewModel(private val dataSource: TvShowDbDAO) : ViewModel() {

    /**
     * Get the user name of the user.
     * @return a [Flowable] that will emit every time the user name has been updated.
     */
    // for every emission of the user, get the user name
    fun detailTvShow(tvId: Int?): Flowable<ArrayList<TvShowsDAO>> {
        return dataSource.getTvShowById(tvId)
            .map {
                arrayListOf(
                    TvShowsDAO(
                        it.id,
                        it.title,
                        it.rating,
                        it.releaseDate,
                        it.poster,
                        it.desc,
                        it.backdrop
                    )
                )
            }
    }

    /**
     * Update the user name.
     * @param userName the new user name
     * *
     * @return a [Completable] that completes when the user name is updated
     */
    fun updateTvShow(tv: TvShowsDAO): Completable {
        val m = TvShow(
            tv.id,
            tv.title,
            tv.rating,
            tv.releaseDate,
            tv.poster,
            tv.desc,
            tv.backdrop
        )

        return dataSource.insertTvShow(m)
    }

    fun deleteTvShow(id: Int?): Completable {
        return dataSource.deleteTvShow(id)
    }

    fun getTvShows(): Flowable<List<TvShow>> {
        return dataSource.getTvShows()
    }
}
