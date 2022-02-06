package com.yodgorbek.movietask.presentation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yodgorbek.movietask.domain.model.Movie
import com.yodgorbek.movietask.repository.MovieRepository
import com.yodgorbek.movietask.util.Constants
import com.yodgorbek.movietask.util.Event
import com.yodgorbek.movietask.util.Resource
import com.yodgorbek.movietask.util.network.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val mNetworkHelper: NetworkHelper,
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _movies = MutableLiveData<Event<Resource<List<Movie>>>>()
    val movies: LiveData<Event<Resource<List<Movie>>>> get() = _movies

    private val _moviesFromDb = MutableLiveData<List<Movie>>()
    val moviesFromDb: LiveData<List<Movie>> get() = _moviesFromDb

    fun getMovies(searchQuery: String) {
        Timber.d("getMovies...")
        _movies.postValue(Event(Resource.loading(null)))
        if (searchQuery.isBlank()) {
            _movies.postValue(Event(Resource.error("The fields must not be empty", null)))
            return
        }
        if (searchQuery.length > Constants.MAX_MOVIE_TITLE_LENGTH) {
            _movies.postValue(
                Event(
                    Resource.error(
                        "The title can't be longer than ${Constants.MAX_MOVIE_TITLE_LENGTH}",
                        null
                    )
                )
            )
            return
        }
        getMoviesFromNetwork(searchQuery)
    }

    private fun getMoviesFromNetwork(searchQuery: String) = viewModelScope.launch {
        if (mNetworkHelper.isNetworkConnected()) {
            movieRepository.getMovies(searchQuery).collect {
                _movies.postValue(Event(it))
            }
        } else {
            _movies.postValue(
                Event(
                    Resource.error(
                        Constants.ERROR_INTERNET_CONNECTION,
                        null
                    )
                )
            )
        }
    }

    fun returnAllMoviesFromDb() = viewModelScope.launch {
        movieRepository.returnAllMovies().collect {
            _moviesFromDb.postValue(it)
        }
    }

    fun insertMovieList(movieList: List<Movie>) = viewModelScope.launch {
        movieRepository.insertMovieList(movieList = movieList)
    }

    fun insertMovie(movie: Movie) = viewModelScope.launch {
        movieRepository.insertMovie(movie)
    }

    fun deleteMovie(movie: Movie) = viewModelScope.launch {
        movieRepository.deleteMovie(movie)
    }

    fun deleteMovieList(movieList: List<Movie>) = viewModelScope.launch {
        movieRepository.deleteMovieList(movieList)
    }

}