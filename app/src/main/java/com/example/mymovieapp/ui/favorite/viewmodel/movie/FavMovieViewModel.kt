package com.example.mymovieapp.ui.favorite.viewmodel.movie

import androidx.lifecycle.ViewModel
import com.example.mymovieapp.database.movie.MoviesDAO
import com.example.mymovieapp.database.persistance.movie.Movie
import com.example.mymovieapp.database.persistance.movie.MovieDbDAO
import io.reactivex.Completable
import io.reactivex.Flowable

class FavMovieViewModel(private val dataSource: MovieDbDAO) : ViewModel() {

    fun detailMovie(movieId: Int?): Flowable<ArrayList<MoviesDAO>> {
        return dataSource.getMovieById(movieId)
            .map {
                arrayListOf(
                    MoviesDAO(
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

    fun updateMovie(movie: MoviesDAO): Completable {
        val m = Movie(
            movie.id,
            movie.title,
            movie.rating,
            movie.releaseDate,
            movie.poster,
            movie.desc,
            movie.backdrop
        )

        return dataSource.insertMovie(m)
    }

    fun deleteMovie(id: Int?): Completable {
        return dataSource.deleteMovie(id)
    }

    fun getMovies(): Flowable<List<Movie>> {
        return dataSource.getMovies()
    }
}
