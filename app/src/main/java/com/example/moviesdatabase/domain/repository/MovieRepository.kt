package com.example.moviesdatabase.domain.repository

import com.example.moviesdatabase.domain.util.Resource
import com.example.moviesdatabase.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getTrendingMovies(): Flow<Resource<List<Movie>>>
    fun getNowPlayingMovies(): Flow<Resource<List<Movie>>>
    suspend fun searchMovies(query: String) : Resource<List<Movie>>
    suspend fun toggleBookmark(movie: Movie)
    fun getBookmarkedMovies(): Flow<Resource<List<Movie>>>
}