package com.example.moviesdatabase.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesdatabase.domain.usecases.SearchMoviesUseCase
import com.example.moviesdatabase.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    init {
        observeSearchQuery()
    }

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeSearchQuery() {
        _query
            .debounce(500L)
            .distinctUntilChanged()
            .filter { it.isNotBlank() }
            .onEach { _uiState.value = SearchUiState.Loading }
            .flatMapLatest {
                flow { emit(searchMoviesUseCase(it)) }
            }
            .onEach { result ->
                when(result) {
                    is Resource.Loading -> _uiState.value = SearchUiState.Loading
                    is Resource.Success -> _uiState.value = SearchUiState.Success(result.data ?: emptyList())
                    is Resource.Error -> _uiState.value = SearchUiState.Error(result.message ?: "Unknown error")
                }
            }.launchIn(viewModelScope)
    }
}