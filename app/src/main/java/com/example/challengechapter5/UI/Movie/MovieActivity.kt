package com.example.challengechapter5.UI.Movie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.challengechapter5.API.MovieAuthAPI
import com.example.challengechapter5.API.MovieClient
import com.example.challengechapter5.API.POSTER_BASE_URL
import com.example.challengechapter5.API.Auth.MovieDetails
import com.example.challengechapter5.R
import com.example.challengechapter5.Repository.NetworkState
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textview.MaterialTextView

class MovieActivity : AppCompatActivity() {
    private lateinit var movieRepository: MovieDetailsRepository
    private lateinit var viewModel: MovieViewModel

    private lateinit var progressIndicator: CircularProgressIndicator
    private lateinit var error: MaterialTextView
    private lateinit var title: MaterialTextView
    private lateinit var subtitle: MaterialTextView
    private lateinit var releaseDate: MaterialTextView
    private lateinit var rating: MaterialTextView
    private lateinit var runtime: MaterialTextView
    private lateinit var overview: MaterialTextView
    private lateinit var moviePoster: ImageView
    private lateinit var synopsis: MaterialTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_details)

        initComponents()

        val movieId = intent.getIntExtra("id", 1)
        val apiService: MovieAuthAPI = MovieClient.getClient()

        movieRepository = MovieDetailsRepository(apiService)
        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, androidx.lifecycle.Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, androidx.lifecycle.Observer {
            progressIndicator.visibility =
                if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
                error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    private fun initComponents() {
        progressIndicator = findViewById(R.id.progress_indicator)
        error = findViewById(R.id.error)
        title = findViewById(R.id.title)
        subtitle = findViewById(R.id.subtitle)
        releaseDate = findViewById(R.id.release_date)
        rating = findViewById(R.id.rating)
        runtime = findViewById(R.id.runtime)
        overview = findViewById(R.id.overview)
        moviePoster = findViewById(R.id.movie_poster)

    }

    fun bindUI(it: MovieDetails) {
        title.text = it.title
        subtitle.text = it.tagline
        releaseDate.text = it.releaseDate
        rating.text = it.rating.toString()
        runtime.text = it.runtime.toString() + " minutes"
        overview.text = it.overview

        val posterURL = POSTER_BASE_URL + it.posterPath

        Glide.with(this).load(posterURL).into(moviePoster)
    }

    private fun getViewModel(movieId: Int): MovieViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {

            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieViewModel(movieRepository, movieId) as T
            }
        })[MovieViewModel::class.java]
    }
}
