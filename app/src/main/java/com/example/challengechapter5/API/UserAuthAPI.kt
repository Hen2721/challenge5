package com.example.challengechapter5.API

import com.example.challengechapter5.API.Auth.*
import retrofit2.Response
import retrofit2.http.*

interface UserAuthAPI {
    @POST("users/login")
    suspend fun signIn(@Body request: SignInRequest): Response<SignInResponse>

    @POST("users/register")
    suspend fun signUp(@Body request: SignUpRequest): Response<SignUpResponse>

    @GET("users/logout")
    suspend fun logout(@HeaderMap headers: Map<String, String>): Response<Unit>

    @PUT("users/{ID}")
    suspend fun updateProfile(
        @Path("ID") id: String,
        @Body request: UpdateProfileRequest): Response<SignInResponse>
}