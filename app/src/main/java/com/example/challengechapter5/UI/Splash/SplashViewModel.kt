package com.example.challengechapter5.UI.Splash

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.challengechapter5.Data

class SplashViewModel: ViewModel() {
    val shouldOpenOnBoarding: MutableLiveData<Boolean> = MutableLiveData()
    val shouldOpenHomePage: MutableLiveData<Boolean> = MutableLiveData()

    fun onViewLoaded(sharedPreferences: SharedPreferences) {
        if (sharedPreferences.getString(Data.Preferences.KEY.TOKEN, "").isNullOrEmpty()) {
            shouldOpenOnBoarding.postValue(true)
        } else {
            shouldOpenHomePage.postValue(true)
        }
    }
}