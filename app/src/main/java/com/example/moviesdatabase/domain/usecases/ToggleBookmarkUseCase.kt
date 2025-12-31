package com.example.moviesdatabase.domain.usecases

import com.example.moviesdatabase.domain.model.Movie
import com.example.moviesdatabase.domain.repository.MovieRepository
import javax.inject.Inject

class ToggleBookmarkUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movie: Movie) {
        repository.toggleBookmark(movie)
    }
}