package com.example.challengechapter5.UI.Movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.challengechapter5.API.Auth.Movie
import com.example.challengechapter5.Repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieActivityViewModel(private val movieRepository: MoviePagedListRepository) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val moviePagedList: LiveData<PagedList<Movie>> by lazy {
        movieRepository.fetchMoviePagedList(compositeDisposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.dispose()
    }
}