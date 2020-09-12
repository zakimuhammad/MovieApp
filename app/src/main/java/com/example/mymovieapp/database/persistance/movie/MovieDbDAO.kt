package com.example.mymovieapp.database.persistance.movie

import android.database.Cursor
import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface MovieDbDAO {

    @get:Query("SELECT * FROM movies")
    val all: List<Movie>

    @Query("SELECT * FROM Movies WHERE movieId = :id")
    fun getMovieById(id: Int?): Flowable<Movie>

    @Query("SELECT * FROM Movies")
    fun getMovies(): Flowable<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie): Long

    @Query("DELETE FROM Movies WHERE movieId = :id")
    fun deleteMovie(id: Int?): Completable

    @Query("SELECT * FROM movies")
    fun allFavorite(): Cursor

    @Query("SELECT * FROM movies WHERE movieId = :id")
    fun getFavoriteById(id: Long): Cursor

    @Update
    fun update(movie: Movie): Int

    @Query("DELETE FROM movies WHERE movieId = :id")
    fun deleteById(id: Long): Int
}
