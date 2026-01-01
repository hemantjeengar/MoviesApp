package com.example.moviesdatabase.data.mappers

import com.example.moviesdatabase.data.local.entity.MovieEntity
import com.example.moviesdatabase.data.remote.dto.MovieDto
import com.example.moviesdatabase.domain.model.Movie
import com.example.moviesdatabase.domain.model.MovieCategory

fun MovieDto.toEntity(category: String): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath ?: "",
        voteAverage = voteAverage,
        category = category,
        isBookmarked = false
    )
}

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath ?: "",
        voteAverage = voteAverage,
        category = MovieCategory.GENERAL.key,
        isBookmarked = false
    )
}

fun MovieEntity.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        voteAverage = voteAverage,
        category = category,
        isBookmarked = isBookmarked
    )
}

fun Movie.toEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        voteAverage = voteAverage,
        category = category,
        isBookmarked = isBookmarked
    )
}