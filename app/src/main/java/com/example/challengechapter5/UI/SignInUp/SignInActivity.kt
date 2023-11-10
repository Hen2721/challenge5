package com.example.challengechapter5.UI.SignInUp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.example.challengechapter5.Data
import com.example.challengechapter5.Database.DatabaseUser
import com.example.challengechapter5.R
import com.example.challengechapter5.UI.Movie.MainActivity
import com.example.challengechapter5.databinding.ActivitySignInBinding
import com.google.android.material.snackbar.Snackbar

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = DatabaseUser.getInstance(this.applicationContext)
        val pref = this.getSharedPreferences(Data.Preferences.PREF_NAME, MODE_PRIVATE)

        viewModel.onViewLoaded(db, pref)

        bindViewModel()
        bindView()
    }

    private fun bindViewModel(){
        viewModel.shouldShowError.observe(this){
            val snackbar = Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG)
            snackbar.view.setBackgroundColor(Color.RED)
            snackbar.show()
        }

        viewModel.shouldOpenHomePage.observe(this) {
            if (it) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun bindView() {
        binding.etEmailSignIn.doAfterTextChanged {
            viewModel.onChangeEmail(it.toString())
        }

        binding.etPassSignIn.doAfterTextChanged {
            viewModel.onChangePassword(it.toString())
        }

        binding.btnSignIn2.setOnClickListener {
            viewModel.onClickSignIn()
        }
        binding.tvGoToSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}