package com.example.challengechapter5.Repository

data class Resource<out T>(val status: StatusProfile, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): Resource<T> =
            Resource(status = StatusProfile.SUCCESS, data = data, message = null)

        fun <T> error(data: T?, message: String): Resource<T> =
            Resource(status = StatusProfile.ERROR, data = data, message = message)

        fun <T> loading(data: T?): Resource<T> =
            Resource(status = StatusProfile.LOADING, data = data, message = null)
    }
}

enum class StatusProfile {
    SUCCESS,
    ERROR,
    LOADING
}
