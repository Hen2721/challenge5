package com.example.challengechapter5.UI.Movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.challengechapter5.API.MovieAuthAPI
import com.example.challengechapter5.API.POST_PER_PAGE
import com.example.challengechapter5.API.Auth.Movie
import com.example.challengechapter5.Repository.MovieDataSource
import com.example.challengechapter5.Repository.MovieDataSourceFactory
import com.example.challengechapter5.Repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepository(private val apiService: MovieAuthAPI) {

    lateinit var movieDataSourceFactory: MovieDataSourceFactory
    lateinit var moviePagedList: LiveData<PagedList<Movie>>

    fun fetchMoviePagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<Movie>> {
        movieDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(movieDataSourceFactory, config).build()
        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            movieDataSourceFactory.moviesLiveDataSource, MovieDataSource::networkState
        )
    }
}