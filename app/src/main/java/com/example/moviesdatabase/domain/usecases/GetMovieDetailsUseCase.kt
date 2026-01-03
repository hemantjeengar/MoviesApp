package com.example.moviesdatabase.domain.usecases

import com.example.moviesdatabase.domain.model.Movie
import com.example.moviesdatabase.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(movieId: Int): Flow<Movie?> {
        return repository.getMovieDetails(movieId)
    }
}