package com.example.moviesdatabase.di

import com.example.moviesdatabase.data.repository.MovieRepositoryImpl
import com.example.moviesdatabase.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(
    SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository
}