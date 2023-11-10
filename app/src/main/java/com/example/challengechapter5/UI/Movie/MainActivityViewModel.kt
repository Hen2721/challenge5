package com.example.challengechapter5.UI.Movie

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.challengechapter5.API.Auth.ErrorResponse
import com.example.challengechapter5.API.UserClient
import com.example.challengechapter5.Data
import com.example.challengechapter5.Database.DatabaseUser
import com.example.challengechapter5.Repository.ProfileRepository
import com.example.challengechapter5.UI.SignInUp.ProfileModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody

class MainActivityViewModel(private val repository: ProfileRepository) : ViewModel() {
    private var db: DatabaseUser? = null
    private var pref: SharedPreferences? = null

    val shouldShowError: MutableLiveData<String> = MutableLiveData()
    val shouldShowUsername: MutableLiveData<String> = MutableLiveData()
    val shouldShowImageProfile: MutableLiveData<ProfileModel> = MutableLiveData()
    val shouldShowLoading: MutableLiveData<Boolean> = MutableLiveData()
    val shouldShowGetStarted: MutableLiveData<Boolean> = MutableLiveData()

    fun onViewLoaded(db: DatabaseUser, preferences: SharedPreferences) {
        this.db = db
        this.pref = preferences

        getProfile()
        getUsername()
    }

    private fun clearToken() {
        viewModelScope.launch {
            repository.deleteProfile()
            withContext(Dispatchers.Main) {
                shouldShowGetStarted.postValue(true)
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

    private fun getUsername() {
        CoroutineScope(Dispatchers.Main).launch {
            val result = db?.userDAO()?.getUser()
            withContext(Dispatchers.Main) {
                result?.let {
                    shouldShowUsername.postValue(it.name)
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
    class Factory(
        private val repository: ProfileRepository
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
                return MainActivityViewModel(
                    repository
                ) as T
            }
            throw IllegalArgumentException("Unknown class name")
        }
    }
}