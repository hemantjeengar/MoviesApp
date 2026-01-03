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
import java.io.IOException
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
                dao.upsertMoviesSafely(entities)
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
                dao.upsertMoviesSafely(entities)
            }
        )
    }

    override suspend fun searchMovies(query: String): Resource<List<Movie>> {
        return try {
            //try online search first
            val response = api.searchMovies(query)
            val entities = response.results.map { it.toEntity(MovieCategory.GENERAL.key) }

            dao.upsertMoviesSafely(entities)

            val movies = entities.map { it.toDomain() }
            Resource.Success(movies)
        } catch (e: Exception) {
            if (e is IOException) {
                //try offline search
                val localMovies = dao.searchMoviesLocally(query)
                if (localMovies.isNotEmpty()) {
                    Resource.Success(localMovies.map { it.toDomain() })
                } else {
                    Resource.Error("No internet and no local results found.")
                }
            } else {
                Resource.Error("Could not search: ${e.message}")
            }
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

    override fun getMovieDetails(movieId: Int): Flow<Movie?> {
        return dao.getMovie(movieId).map { entity ->
            entity?.toDomain() }
    }
}