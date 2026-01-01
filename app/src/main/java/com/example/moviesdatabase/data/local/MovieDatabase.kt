package com.example.moviesdatabase.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviesdatabase.data.local.entity.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = 1
)
abstract class MovieDatabase: RoomDatabase() {
    abstract val movieDao: MovieDao
}