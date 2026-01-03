package com.example.moviesdatabase.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.moviesdatabase.data.local.entity.BookmarkTuple
import com.example.moviesdatabase.data.local.entity.MovieEntity
import com.example.moviesdatabase.data.local.entity.NowPlayingEntity
import com.example.moviesdatabase.data.local.entity.TrendingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMovies(movies: List<MovieEntity>)

    @Query("SELECT id, isBookmarked FROM movies WHERE id in (:movieIds)")
    suspend fun getBulkBookmarkStatus(movieIds: List<Int>): List<BookmarkTuple>

    @Transaction
    suspend fun upsertMoviesSafely(newMovies: List<MovieEntity>) {
        //get ids of the new movies
        val newIds = newMovies.map { it.id }

        //get current bookmark status of these movies
        val currentBookmarkStatus = getBulkBookmarkStatus(newIds)
            .associate { it.id to it.isBookmarked }

        //merge the new movies with the current bookmark status
        val mergedMovies = newMovies.map { movie ->
            val isBookmarkedLocally = currentBookmarkStatus[movie.id] ?: false
            movie.copy(isBookmarked = isBookmarkedLocally)
        }

        //insert all the movies
        insertAllMovies(mergedMovies)
    }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrendingLinks(links: List<TrendingEntity>)

    @Query("DELETE FROM trending_movies")
    suspend fun clearTrending()

    @Query("SELECT m.* FROM movies m INNER JOIN trending_movies t ON m.id = t.movieId ORDER BY t.rank ASC")
    fun getTrendingMovies(): Flow<List<MovieEntity>>

    @Transaction
    suspend fun updateTrendingFeed(movies: List<MovieEntity>, links: List<TrendingEntity>) {
        upsertMoviesSafely(movies)

        clearTrending()
        insertTrendingLinks(links)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNowPlayingLinks(links: List<NowPlayingEntity>)

    @Query("DELETE FROM now_playing_movies")
    suspend fun clearNowPlaying()


    @Query("SELECT m.* FROM movies m INNER JOIN now_playing_movies t ON m.id = t.movieId ORDER BY t.rank ASC")
    fun getNowPlayingMovies(): Flow<List<MovieEntity>>

    @Transaction
    suspend fun updateNowPlayingFeed(movies: List<MovieEntity>, links: List<NowPlayingEntity>) {
        upsertMoviesSafely(movies)

        clearNowPlaying()
        insertNowPlayingLinks(links)
    }


    @Query("SELECT * FROM movies where isBookmarked = 1")
    fun getBookmarkedMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies where id = :movieId")
    fun getMovie(movieId: Int): Flow<MovieEntity?>

    @Update
    suspend fun updateMovie(movieEntity: MovieEntity)

    @Query("SELECT * FROM movies where title LIKE '%' || :query || '%' ")
    suspend fun searchMoviesLocally(query: String): List<MovieEntity>
}