package com.example.challengechapter5.UI.Movie

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.challengechapter5.API.ImageClient
import com.example.challengechapter5.API.MovieAuthAPI
import com.example.challengechapter5.API.MovieClient
import com.example.challengechapter5.API.UserClient
import com.example.challengechapter5.Data
import com.example.challengechapter5.Database.DatabaseUser
import com.example.challengechapter5.R
import com.example.challengechapter5.Repository.NetworkState
import com.example.challengechapter5.Repository.ProfileRepository
import com.example.challengechapter5.UI.SignInUp.ProfileActivity
import com.example.challengechapter5.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val progressDialog: ProgressDialog by lazy { ProgressDialog(this) }
    private val viewModel2: MainActivityViewModel by viewModels{
        MainActivityViewModel.Factory(
            ProfileRepository(
                imageAPI = ImageClient.instanceImage,
                authAPI = UserClient.instanceAuth,
                db = DatabaseUser.getInstance(this)
            )
        )
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: MaterialTextView

    lateinit var movieRepository: MoviePagedListRepository
    private lateinit var viewModel: MovieActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = findViewById(R.id.movie_list)
        progressBar = findViewById(R.id.progress_bar_popular)
        errorTextView = findViewById(R.id.error_popular)

        val apiService: MovieAuthAPI = MovieClient.getClient()
        movieRepository = MoviePagedListRepository(apiService)
        viewModel = getViewModel()

        val movieAdapter = MoviePagedListAdapter(this)

        recyclerView.apply {
            setHasFixedSize(true)
            adapter = movieAdapter
        }

        viewModel.moviePagedList.observe(this, Observer { movieAdapter.submitList(it) })
        viewModel.networkState.observe(this, Observer {
            progressBar.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE

            errorTextView.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) movieAdapter.setNetworkState(it)
        })

        bindView()
        bindViewModel()

        val db = DatabaseUser.getInstance(this)
        val pref = this.getSharedPreferences(
            Data.Preferences.PREF_NAME,
            AppCompatActivity.MODE_PRIVATE
        )
        viewModel2.onViewLoaded(db = db, preferences = pref)
    }

    private fun bindView() {
        binding.rivProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

    }

    private fun bindViewModel() {
        viewModel2.shouldShowError.observe(this) {
            val snackbar = Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG)
            snackbar.view.setBackgroundColor(Color.RED)
            snackbar.show()
        }

        viewModel2.shouldShowLoading.observe(this) {
            if (it) {
                progressDialog.setMessage("Loading...")
                progressDialog.show()
            } else {
                progressDialog.hide()
            }
        }

        viewModel2.shouldShowImageProfile.observe(this) {
            Glide.with(binding.root)
                .load(it)
                .circleCrop()
                .into(binding.rivProfile)
        }

        viewModel2.shouldShowUsername.observe(this) { shouldShowUsername ->
            binding.tvHello.isVisible = !shouldShowUsername.isNullOrEmpty()
            binding.tvHello.text = shouldShowUsername
        }

        viewModel2.shouldShowGetStarted.observe(this) {
            if (it) {
                val intent = Intent(Activity(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    private fun getViewModel(): MovieActivityViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieActivityViewModel(movieRepository) as T
            }
        })[MovieActivityViewModel::class.java]
    }
}