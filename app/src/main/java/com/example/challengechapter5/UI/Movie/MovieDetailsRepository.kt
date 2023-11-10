package com.example.challengechapter5.UI.Movie

import androidx.lifecycle.LiveData
import com.example.challengechapter5.API.MovieAuthAPI
import com.example.challengechapter5.API.Auth.MovieDetails
import com.example.challengechapter5.Repository.MovieDetailsNetworkDataSource
import com.example.challengechapter5.Repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService: MovieAuthAPI) {
    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchMovieDetails(
        compositeDisposable: CompositeDisposable,
        movieId: Int
    ): LiveData<MovieDetails> {
        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieDetailsResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }
}