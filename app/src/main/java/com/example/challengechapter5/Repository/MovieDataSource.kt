package com.example.challengechapter5.Repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.challengechapter5.API.FIRST_PAGE
import com.example.challengechapter5.API.MovieAuthAPI
import com.example.challengechapter5.API.Auth.Movie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource(
    private val apiService: MovieAuthAPI,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Movie>() {

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()
    private var page = FIRST_PAGE

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getPopularMovie(page)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    callback.onResult(it.movieList, null, page + 1)
                    networkState.postValue(NetworkState.LOADED)
                }, {
                    networkState.postValue(NetworkState.ERROR)
                })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getPopularMovie(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    // If there is content to load, load it, else - the end of the list:
                    if (it.totalPages >= params.key) {
                        callback.onResult(it.movieList, params.key + 1)
                        networkState.postValue(NetworkState.LOADED)
                    } else {
                        networkState.postValue(NetworkState.ENDOFLIST)
                    }
                }, {
                    networkState.postValue(NetworkState.ERROR)
                })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {

    }
}