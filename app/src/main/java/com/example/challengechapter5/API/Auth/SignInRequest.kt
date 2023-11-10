package com.example.challengechapter5.API.Auth

import com.google.gson.annotations.SerializedName

data class SignInRequest(
    @SerializedName("login") var login: String? = null,
    @SerializedName("password") var password: String? = null
)