package com.yodgorbek.movietask.data.local

import androidx.room.*
import com.yodgorbek.movietask.data.local.model.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovieList(movieList: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)

    @Delete
    suspend fun deleteMovieList(movieList: List<MovieEntity>)

    @Query("SELECT * FROM movie")
    fun returnAllMovies(): Flow<List<MovieEntity>>

}