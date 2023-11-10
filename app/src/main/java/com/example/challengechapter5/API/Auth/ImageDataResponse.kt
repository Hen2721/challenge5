package com.example.challengechapter5.API.Auth

import com.google.gson.annotations.SerializedName

data class ImageDataResponse(
    @SerializedName("data") var data: DataResponse,
    @SerializedName("success") var success: Boolean,
    @SerializedName("status") var status: Int
) {
    data class DataResponse(
        @SerializedName("id") var id: String,
        @SerializedName("title") var title: String,
        @SerializedName("url_viewer") var urlViewer: String,
        @SerializedName("url") var url: String,
        @SerializedName("display_url") var displayUrl: String,
        @SerializedName("size") var size: String,
        @SerializedName("time") var time: String,
        @SerializedName("expiration") var expiration: String,
        @SerializedName("image") var image: ImageResponse,
        @SerializedName("medium") var medium: MediumResponse,
        @SerializedName("thumb") var thumb: ThumbResponse,
        @SerializedName("delete_url") var deleteUrl: String
    )

    data class ImageResponse(
        @SerializedName("filename") var filename: String,
        @SerializedName("name") var name: String,
        @SerializedName("mime") var mime: String,
        @SerializedName("extension") var extension: String,
        @SerializedName("url") var url: String
    )

    data class ThumbResponse(
        @SerializedName("filename") var filename: String,
        @SerializedName("name") var name: String,
        @SerializedName("mime") var mime: String,
        @SerializedName("extension") var extension: String,
        @SerializedName("url") var url: String
    )

    data class MediumResponse(
        @SerializedName("filename") var filename: String,
        @SerializedName("name") var name: String,
        @SerializedName("mime") var mime: String,
        @SerializedName("extension") var extension: String,
        @SerializedName("url") var url: String
    )
}