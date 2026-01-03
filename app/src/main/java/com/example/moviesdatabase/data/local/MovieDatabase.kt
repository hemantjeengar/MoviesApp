package com.example.moviesdatabase.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviesdatabase.data.local.entity.MovieEntity
import com.example.moviesdatabase.data.local.entity.NowPlayingEntity
import com.example.moviesdatabase.data.local.entity.TrendingEntity

@Database(
    entities = [MovieEntity::class, TrendingEntity::class, NowPlayingEntity::class],
    version = 1
)
abstract class MovieDatabase: RoomDatabase() {
    abstract val movieDao: MovieDao
}