package com.example.challengechapter5.Repository

import com.example.challengechapter5.API.Auth.SignInResponse
import com.example.challengechapter5.API.Auth.UpdateProfileRequest
import com.example.challengechapter5.API.ImageAuthAPI
import com.example.challengechapter5.API.Auth.ImageDataResponse
import com.example.challengechapter5.API.UserAuthAPI
import com.example.challengechapter5.Database.DatabaseUser
import com.example.challengechapter5.Database.local.UserEntity
import com.example.challengechapter5.UI.SignInUp.ProfileModel
import okhttp3.MultipartBody

class ProfileRepository(
    private val imageAPI: ImageAuthAPI,
    private val authAPI: UserAuthAPI,
    private val db: DatabaseUser,
) {
    suspend fun uploadImage(image: MultipartBody.Part): Resource<ImageDataResponse> {
        imageAPI.uploadImage(image = image).let {
            if (it.isSuccessful) {
                updateImageProfile(it.body()?.data?.thumb?.url.orEmpty())
                return Resource(
                    status = StatusProfile.SUCCESS,
                    data = it.body(),
                    message = null
                )
            } else {
                return Resource(
                    status = StatusProfile.ERROR,
                    data = null,
                    message = it.errorBody().toString()
                )
            }
        }
    }

    suspend fun updateProfile(image: String): Resource<SignInResponse> {
        val profile = getProfile()
        val request = UpdateProfileRequest(
            name = profile.name,
            image = image,
            job = profile.job
        )

        return authAPI.updateProfile(
            id = profile.id,
            request = request
        ).let {
            if (it.isSuccessful) {
                Resource(
                    status = StatusProfile.SUCCESS,
                    data = it.body(),
                    message = null
                )
            } else {
                Resource(
                    status = StatusProfile.ERROR,
                    data = null,
                    message = it.errorBody().toString()
                )
            }
        }
    }

    suspend fun getProfile(): ProfileModel {
        return db.userDAO().getUser().let {
            ProfileModel(
                id = it?.id.orEmpty(),
                name = it?.name.orEmpty(),
                job = it?.job.orEmpty(),
                image = it?.image.orEmpty()
            )
        }
    }

    suspend fun updateImageProfile(image: String): Long {
        val profile = db.userDAO().getUser()
        val updatedProfile = UserEntity(
            id = profile?.id.orEmpty(),
            email = profile?.email.orEmpty(),
            name = profile?.name.orEmpty(),
            job = profile?.job.orEmpty(),
            image = image
        )
        return db.userDAO().insertUser(updatedProfile)
    }

    suspend fun deleteProfile(): Int {
        val profile = db.userDAO().getUser()
        val deleteProfile = UserEntity(
            id = profile?.id.orEmpty(),
            email = profile?.email.orEmpty(),
            name = profile?.name.orEmpty(),
            job = profile?.job.orEmpty(),
            image = profile?.image.orEmpty()
        )
        return db.userDAO().deleteUser(deleteProfile)
    }
}