package com.example.moviesdatabase.domain.usecases

import com.example.moviesdatabase.domain.model.Movie
import com.example.moviesdatabase.domain.repository.MovieRepository
import com.example.moviesdatabase.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTrendingMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<Resource<List<Movie>>> {
        return repository.getTrendingMovies()
    }
}