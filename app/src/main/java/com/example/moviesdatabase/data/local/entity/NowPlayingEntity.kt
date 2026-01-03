package com.example.moviesdatabase.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "now_playing_movies",
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("movieId")]
)
data class NowPlayingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val movieId: Int,
    val rank: Int
)