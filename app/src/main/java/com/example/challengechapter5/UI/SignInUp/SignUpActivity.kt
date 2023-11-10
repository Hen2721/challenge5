package com.example.challengechapter5.UI.SignInUp

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.example.challengechapter5.Data
import com.example.challengechapter5.Database.DatabaseUser
import com.example.challengechapter5.UI.Movie.MainActivity
import com.example.challengechapter5.databinding.ActivitySignUpBinding
import com.google.android.material.snackbar.Snackbar

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModels()
    private val progressDialog: ProgressDialog by lazy { ProgressDialog(this) }
    private val progressBar: ProgressBar by lazy { ProgressBar(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindView()
        bindViewModel()

        val db = DatabaseUser.getInstance(this.applicationContext)
        val pref = this.getSharedPreferences(Data.Preferences.PREF_NAME, MODE_PRIVATE)
        viewModel.onViewLoaded(db, pref)
    }

    private fun bindView() {
        binding.etFullName.doAfterTextChanged {
            viewModel.onChangeName(it.toString())
        }
        binding.etJob.doAfterTextChanged {
            viewModel.onChangeJob(it.toString())
        }
        binding.etEmailSignUp.doAfterTextChanged {
            viewModel.onChangeEmail(it.toString())
        }
        binding.etPassSignUp.doAfterTextChanged {
            viewModel.onChangePassword(it.toString())
        }
        binding.btnContinue.setOnClickListener {
            viewModel.onValidate()
        }
    }

    private fun bindViewModel() {
        viewModel.shouldShowError.observe(this) {
            val snackbar = Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG)
            snackbar.view.setBackgroundColor(Color.RED)
            snackbar.show()
        }

        viewModel.shouldShowLoading.observe(this) {
            if (it) {
                progressDialog.setMessage("Loading...")
                progressDialog.show()
            } else {
                progressDialog.hide()
            }
        }

        viewModel.shouldOpenUpdateProfile.observe(this) {
            if (it) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}