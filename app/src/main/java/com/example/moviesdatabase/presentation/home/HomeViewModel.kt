package com.example.moviesdatabase.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesdatabase.domain.usecases.GetNowPlayingMoviesUseCase
import com.example.moviesdatabase.domain.usecases.GetTrendingMoviesUseCase
import com.example.moviesdatabase.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                getTrendingMoviesUseCase(),
                getNowPlayingMoviesUseCase()
            ) { trendingRes, nowPlayingRes ->
                val isLoading = trendingRes is Resource.Loading || nowPlayingRes is Resource.Loading
                val error = trendingRes.message ?: nowPlayingRes.message

                HomeUiState(
                    isLoading = isLoading,
                    trendingMovies = trendingRes.data ?: emptyList(),
                    nowPlayingMovies = nowPlayingRes.data ?: emptyList(),
                    error = error
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}