package com.example.moviesdatabase.presentation.search

import com.example.moviesdatabase.domain.model.Movie

sealed class SearchUiState {
    object Idle: SearchUiState()
    object Loading: SearchUiState()
    data class Success(val movies: List<Movie>): SearchUiState()
    data class Error(val message: String): SearchUiState()
}
