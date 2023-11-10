package com.example.challengechapter5.UI.Splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.viewModels
import com.example.challengechapter5.Data
import com.example.challengechapter5.UI.Movie.MainActivity
import com.example.challengechapter5.UI.SignInUp.SignInActivity
import com.example.challengechapter5.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = this.getSharedPreferences(Data.Preferences.PREF_NAME, MODE_PRIVATE)
        val timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                viewModel.onViewLoaded(pref)
            }
        }
        timer.start()

        bindViewModel()
        bindView()
    }

    private fun bindViewModel() {
        viewModel.shouldOpenOnBoarding.observe(this) {
            if (it) {
                val intent = Intent(this, SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        viewModel.shouldOpenHomePage.observe(this) {
            if (it) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

    }

    private fun bindView() {

    }
}