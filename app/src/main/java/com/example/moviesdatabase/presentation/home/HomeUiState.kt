package com.example.moviesdatabase.presentation.home

import com.example.moviesdatabase.domain.model.Movie

data class HomeUiState(
    val isLoading: Boolean = false,
    val trendingMovies: List<Movie> = emptyList(),
    val nowPlayingMovies: List<Movie> = emptyList(),
    val error: String? = null
)
