package com.example.challengechapter5.UI.SignInUp

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.challengechapter5.API.Auth.ErrorResponse
import com.example.challengechapter5.API.UserClient
import com.example.challengechapter5.Data
import com.example.challengechapter5.Database.DatabaseUser
import com.example.challengechapter5.Repository.ProfileRepository
import com.example.challengechapter5.Repository.StatusProfile
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.ResponseBody

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {
    private var db: DatabaseUser? = null
    private var pref: SharedPreferences? = null

    val shouldShowError: MutableLiveData<String> = MutableLiveData()
    val shouldShowImage: MutableLiveData<String> = MutableLiveData()
    val shouldShowImageProfile: MutableLiveData<ProfileModel> = MutableLiveData()
    val shouldShowName: MutableLiveData<String> = MutableLiveData()
    val shouldShowJob: MutableLiveData<String> = MutableLiveData()
    val shouldShowEmail: MutableLiveData<String> = MutableLiveData()
    val shouldShowLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun onViewLoaded(db: DatabaseUser, preferences: SharedPreferences) {
        this.db = db
        this.pref = preferences

        getProfile()
        getName()
        getJob()
        getEmail()
    }

    fun uploadImage(body: MultipartBody.Part) {
        shouldShowLoading.postValue(true)
        CoroutineScope(Dispatchers.IO).launch {
            val result = repository.uploadImage(body)
            withContext(Dispatchers.Main) {
                shouldShowLoading.postValue(false)
                when (result.status) {
                    StatusProfile.SUCCESS -> {
                        shouldShowImage.postValue(result.data?.data?.thumb?.url)
                    }
                    StatusProfile.ERROR -> {
                        shouldShowError.postValue(result.message.orEmpty())
                    }
                    StatusProfile.LOADING -> {

                    }
                }
            }
        }
    }

    fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            shouldShowLoading.postValue(true)
            val headers = mapOf(
                "user-token" to pref?.getString(Data.Preferences.KEY.TOKEN, "").orEmpty()
            )
            val result = UserClient.instanceAuth.logout(headers = headers)
            withContext(Dispatchers.Main) {
                if (result.isSuccessful) {
                    // clear data profile dari room
                    // clear token dari preferences
                    shouldShowLoading.postValue(false)
                } else {
                    showErrorMessage(response = result.errorBody())
                    shouldShowLoading.postValue(false)

                }
            }
        }
    }

    fun getProfile() {
        CoroutineScope(Dispatchers.Main).launch {
            val result = repository.getProfile()
            withContext(Dispatchers.Main) {
                result.let {
                    shouldShowImageProfile.postValue(it)
                }
            }
        }
    }

    private fun getName() {
        CoroutineScope(Dispatchers.Main).launch {
            val result = db?.userDAO()?.getUser()
            withContext(Dispatchers.Main) {
                result?.let {
                    shouldShowName.postValue(it.name)
                } ?: run {
                    showErrorMessage(message = "Data Kosong")
                }
            }
        }
    }

    private fun getJob() {
        CoroutineScope(Dispatchers.Main).launch {
            val result = db?.userDAO()?.getUser()
            withContext(Dispatchers.Main) {
                result?.let {
                    shouldShowJob.postValue(it.job)
                } ?: run {
                    showErrorMessage(message = "Data Kosong")
                }
            }
        }
    }

    private fun getEmail() {
        CoroutineScope(Dispatchers.Main).launch {
            val result = db?.userDAO()?.getUser()
            withContext(Dispatchers.Main) {
                result?.let {
                    shouldShowEmail.postValue(it.email)
                } ?: run {
                    showErrorMessage(message = "Data Kosong")
                }
            }
        }
    }

    private fun showErrorMessage(response: ResponseBody? = null, message: String? = null) {
        val error =
            Gson().fromJson(response?.string() ?: message ?: "", ErrorResponse::class.java)
        shouldShowError.postValue(error.message.orEmpty() + " #${error.code}")
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val repository: ProfileRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                return ProfileViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown class name")
        }
    }
}