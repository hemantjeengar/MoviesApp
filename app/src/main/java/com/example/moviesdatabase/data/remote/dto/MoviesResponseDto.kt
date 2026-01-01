package com.example.moviesdatabase.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MoviesResponseDto(
    @SerializedName("results") val results: List<MovieDto>
)
