package com.example.moviesdatabase.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesdatabase.domain.model.Movie
import com.example.moviesdatabase.domain.usecases.GetMovieDetailsUseCase
import com.example.moviesdatabase.domain.usecases.ToggleBookmarkUseCase
import com.example.moviesdatabase.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val movieId: Int = checkNotNull(savedStateHandle.get<String>("movieId")).toIntOrNull()
        ?: -1

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadMovie()
    }

    fun loadMovie() {
        getMovieDetailsUseCase(movieId)
            .onEach { result ->
                when(result) {
                    is Resource.Loading -> {
                        _uiState.value = DetailUiState.Loading
                    }
                    is Resource.Success -> {
                        result.data?.let { movie ->
                            _uiState.value = DetailUiState.Success(movie)
                        }
                    }
                    is Resource.Error -> {
                        _uiState.value = DetailUiState.Error(result.message ?: "Unknown error")
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onBookmarkClick(movie: Movie) {
        viewModelScope.launch {
            toggleBookmarkUseCase(movie)
        }
    }
}