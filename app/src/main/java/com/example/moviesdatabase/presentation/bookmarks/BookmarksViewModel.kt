package com.example.moviesdatabase.presentation.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesdatabase.domain.model.Movie
import com.example.moviesdatabase.domain.usecases.GetBookmarkedMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val getBookmarkedMoviesUseCase: GetBookmarkedMoviesUseCase
): ViewModel() {

    val bookmarkedMovies: StateFlow<List<Movie>> = getBookmarkedMoviesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}