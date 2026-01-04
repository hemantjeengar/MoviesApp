package com.example.moviesdatabase.presentation.detail

import com.example.moviesdatabase.domain.model.Movie

sealed class DetailUiState {
    object Loading: DetailUiState()
    data class Success(val movie: Movie): DetailUiState()
    data class Error(val message: String): DetailUiState()
}