package com.example.challengechapter5.UI.SignInUp

import android.app.ProgressDialog
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.challengechapter5.API.ImageClient
import com.example.challengechapter5.API.UserClient
import com.example.challengechapter5.Data
import com.example.challengechapter5.Database.DatabaseUser
import com.example.challengechapter5.R
import com.example.challengechapter5.Repository.ProfileRepository
import com.example.challengechapter5.UI.Movie.MainActivityViewModel
import com.example.challengechapter5.UI.Movie.MovieActivityViewModel
import com.example.challengechapter5.databinding.ActivityMainBinding
import com.example.challengechapter5.databinding.ActivityProfileBinding
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    private val progressDialog: ProgressDialog by lazy { ProgressDialog(this) }
    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModel.Factory(
            ProfileRepository(
                imageAPI = ImageClient.instanceImage,
                authAPI = UserClient.instanceAuth,
                db = DatabaseUser.getInstance(this)
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindView()
        bindViewModel()

        viewModel.getProfile()

        val db = DatabaseUser.getInstance(this)
        val pref = this.getSharedPreferences(
            Data.Preferences.PREF_NAME,
            AppCompatActivity.MODE_PRIVATE
        )
        viewModel.onViewLoaded(db = db, preferences = pref)
    }

    private fun bindView() {
        binding.tvLogout.setOnClickListener {
            viewModel.logout()
        }

        val getContent =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    // image/png or jpeg or gif
                    val type = contentResolver.getType(it)
                    // akan terbuat secara otomatis kalau value nya null,> akan di simpan dalam dir cache
                    val tempFile = File.createTempFile("temp-", null, null)
                    val inputstream = contentResolver.openInputStream(uri)

                    tempFile.outputStream().use {
                        inputstream?.copyTo(it)
                    }

                    val requestBody: RequestBody = tempFile.asRequestBody(type?.toMediaType())
                    val body =
                        MultipartBody.Part.createFormData("image", tempFile.name, requestBody)

                    viewModel.uploadImage(body)
                }
            }

        binding.rivProfileUser.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.ivBackProfile.setOnClickListener {
            onBackPressed()
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

        viewModel.shouldShowImage.observe(this) {
            Glide.with(this)
                .load(it)
                .circleCrop()
                .into(binding.rivProfileUser)
        }

        viewModel.shouldShowImageProfile.observe(this) {
            Glide.with(binding.root)
                .load(it)
                .circleCrop()
                .into(binding.rivProfileUser)
        }

        viewModel.shouldShowName.observe(this) { shouldShowName ->
            binding.etFullNameProfile.setText(shouldShowName)
        }

        viewModel.shouldShowJob.observe(this) { shouldShowJob ->
            binding.etJobProfile.setText(shouldShowJob)
        }

        viewModel.shouldShowEmail.observe(this) { shouldShowEmail ->
            binding.etEmailProfile.setText(shouldShowEmail)
        }
    }
}