package com.example.moviesdatabase.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesdatabase.domain.model.Movie
import com.example.moviesdatabase.domain.usecases.GetMovieDetailsUseCase
import com.example.moviesdatabase.domain.usecases.ToggleBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
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

    val movieState: StateFlow<Movie?> = getMovieDetailsUseCase(movieId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun onBookmarkClick(movie: Movie) {
        viewModelScope.launch {
            toggleBookmarkUseCase(movie)
        }
    }
}