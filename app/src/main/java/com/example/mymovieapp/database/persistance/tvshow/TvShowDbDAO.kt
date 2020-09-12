package com.example.mymovieapp.database.persistance.tvshow

import android.database.Cursor
import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface TvShowDbDAO {

    @get:Query("SELECT * FROM tvshows")
    val all: List<TvShow>

    @Query("SELECT * FROM tvshows WHERE tvId = :id")
    fun getTvShowById(id: Int?): Flowable<TvShow>

    @Query("SELECT * FROM tvshows")
    fun getTvShows(): Flowable<List<TvShow>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTvShow(tvShow: TvShow): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tvShow: TvShow): Long

    @Query("DELETE FROM tvshows WHERE tvid = :id")
    fun deleteTvShow(id: Int?): Completable

    @Query("DELETE FROM tvshows WHERE tvId = :id")
    fun deleteById(id: Long): Int

    @Query("SELECT * FROM tvshows")
    fun allFavorite(): Cursor

    @Query("SELECT * FROM tvshows WHERE tvId = :id")
    fun getFavoriteById(id: Long): Cursor

    @Update
    fun update(tvShow: TvShow): Int

}