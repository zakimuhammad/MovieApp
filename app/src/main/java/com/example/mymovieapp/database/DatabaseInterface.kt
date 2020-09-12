package com.example.mymovieapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mymovieapp.database.persistance.movie.Movie
import com.example.mymovieapp.database.persistance.movie.MovieDbDAO
import com.example.mymovieapp.database.persistance.tvshow.TvShow
import com.example.mymovieapp.database.persistance.tvshow.TvShowDbDAO

@Database(entities = [Movie::class, TvShow::class], version = 1, exportSchema = false)
abstract class DatabaseInterface : RoomDatabase() {
    abstract fun tvShowDao(): TvShowDbDAO
    abstract fun movieDao(): MovieDbDAO

    companion object {
        @Volatile
        private var INSTANCE: DatabaseInterface? = null

        fun getInstance(context: Context?): DatabaseInterface =
            INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                        ?: buildDatabase(
                            context
                        ).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context?) =
            Room.databaseBuilder(
                context!!.applicationContext,
                DatabaseInterface::class.java, "Movie.db"
            )
                .allowMainThreadQueries()
                .build()
    }
}