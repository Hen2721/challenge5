package com.example.challengechapter5.Repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.challengechapter5.API.MovieAuthAPI
import com.example.challengechapter5.API.Auth.Movie
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory(
    private val apiService: MovieAuthAPI,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, Movie>() {

    val moviesLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService, compositeDisposable)
        moviesLiveDataSource.postValue(movieDataSource)

        return movieDataSource
    }
}