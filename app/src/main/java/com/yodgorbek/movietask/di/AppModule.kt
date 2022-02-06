package com.yodgorbek.movietask.di

import com.yodgorbek.movietask.data.local.MoviesDao
import com.yodgorbek.movietask.data.local.model.MovieEntityMapper
import com.yodgorbek.movietask.data.remote.MovieApiService
import com.yodgorbek.movietask.data.remote.model.MovieDtoMapper
import com.yodgorbek.movietask.repository.MovieRepository
import com.yodgorbek.movietask.repository.MovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMovieRepository(
        dao: MoviesDao,
        apiService: MovieApiService,
        movieEntityMapper: MovieEntityMapper,
        movieDtoMapper: MovieDtoMapper
    ) = MovieRepositoryImpl(
        moviesDao = dao,
        movieApiService = apiService,
        movieEntityMapper = movieEntityMapper,
        movieDtoMapper = movieDtoMapper
    ) as MovieRepository

}