package com.example.challengechapter5.API

import com.example.challengechapter5.API.Auth.ImageDataResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ImageAuthAPI {
    @POST("upload") // RequestBody
    @Multipart
    suspend fun uploadImage(
        @Query("expiration") expiration: Int = 10000,
        @Query("key") key: String = ImageClient.APIKEY,
        @Part image: MultipartBody.Part
    ): Response<ImageDataResponse>
}