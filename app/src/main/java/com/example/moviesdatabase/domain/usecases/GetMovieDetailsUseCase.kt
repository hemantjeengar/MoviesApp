package com.example.moviesdatabase.domain.usecases

import com.example.moviesdatabase.domain.model.Movie
import com.example.moviesdatabase.domain.repository.MovieRepository
import com.example.moviesdatabase.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(movieId: Int): Flow<Resource<Movie?>> {
        return repository.getMovie(movieId)
    }
}