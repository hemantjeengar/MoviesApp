package com.example.moviesdatabase.data.repository

import com.example.moviesdatabase.data.local.MovieDao
import com.example.moviesdatabase.data.mappers.toDomain
import com.example.moviesdatabase.data.mappers.toEntity
import com.example.moviesdatabase.data.remote.TmdbApi
import com.example.moviesdatabase.data.util.networkBoundResource
import com.example.moviesdatabase.domain.model.Movie
import com.example.moviesdatabase.domain.model.MovieCategory
import com.example.moviesdatabase.domain.repository.MovieRepository
import com.example.moviesdatabase.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: TmdbApi,
    private val dao: MovieDao
): MovieRepository {

    override fun getTrendingMovies(): Flow<Resource<List<Movie>>> {
        return networkBoundResource(
            query = {
                dao.getMoviesByCategory(MovieCategory.TRENDING.key).map { entities ->
                    entities.map { it.toDomain() }
                }
            },
            fetch = { api.getTrendingMovies() },
            saveFetchResult = { response ->
                val entities = response.results.map { it.toEntity(MovieCategory.TRENDING.key) }
                dao.insertMovies(entities)
            }
        )
    }

    override fun getNowPlayingMovies(): Flow<Resource<List<Movie>>> {
        return networkBoundResource(
            query = {
                dao.getMoviesByCategory(MovieCategory.NOW_PLAYING.key).map { entities ->
                    entities.map { it.toDomain() }
                }
            },
            fetch = { api.getTrendingMovies() },
            saveFetchResult = { response ->
                val entities = response.results.map { it.toEntity(MovieCategory.NOW_PLAYING.key) }
                dao.insertMovies(entities)
            }
        )
    }

    override suspend fun searchMovies(query: String): Resource<List<Movie>> {
        return try {
            val response = api.searchMovies(query)
            val movies = response.results.map { it.toDomain() }
            Resource.Success(movies)
        } catch (e: Exception) {
            Resource.Error("Could not search: ${e.message}")
        }
    }

    override suspend fun toggleBookmark(movie: Movie) {
        val updatedMovie = movie.copy(isBookmarked = !movie.isBookmarked)
        dao.updateMovie(updatedMovie.toEntity())
    }

    override fun getBookmarkedMovies(): Flow<List<Movie>> {
        return dao.getBookmarkedMovies().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}