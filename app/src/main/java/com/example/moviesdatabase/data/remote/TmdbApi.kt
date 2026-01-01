package com.example.moviesdatabase.data.remote


import com.example.moviesdatabase.data.remote.dto.MoviesResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {

    @GET("trending/movie/day")
    suspend fun getTrendingMovies(): MoviesResponseDto

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(): MoviesResponseDto

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String): MoviesResponseDto
}