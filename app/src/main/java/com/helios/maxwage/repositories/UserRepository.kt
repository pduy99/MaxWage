package com.helios.maxwage.repositories

import com.helios.maxwage.api.ApiFactory
import com.helios.maxwage.api.Resource
import com.helios.maxwage.api.ResponseHandler
import com.helios.maxwage.models.LoginResponse
import com.helios.maxwage.models.User
import java.util.*

/**
 * Created by Helios on 4/27/2021.
 */
class UserRepository(private val responseHandler: ResponseHandler) {

    companion object {
        private var INSTANCE: UserRepository? = null
        fun getInstance() = INSTANCE
            ?: UserRepository(ResponseHandler()).also {
                INSTANCE = it
            }
    }

    suspend fun login(email: String, password: String): Resource<LoginResponse> {
        return try {
            responseHandler.handleSuccess(ApiFactory.instance.login(email, password))
        } catch (ex: Exception) {
            responseHandler.handleException(ex)
        }
    }

    suspend fun register(
        fullName: String,
        phone: String,
        email: String,
        password: String
    ): Resource<Nothing> {
        return try {
            responseHandler.handleSuccess(
                ApiFactory.instance.register(
                    fullName,
                    phone,
                    email,
                    password
                )
            )
        } catch (ex: Exception) {
            responseHandler.handleException(ex)
        }
    }

    suspend fun getMyProfile(token: String): Resource<User> {
        return try {
            responseHandler.handleSuccess(ApiFactory.instance.getMyProfile("Bearer $token"))
        } catch (ex: Exception) {
            responseHandler.handleException(ex)
        }
    }

    suspend fun getFavoriteJobs(token: String): Resource<List<String>> {
        return try {
            responseHandler.handleSuccess(ApiFactory.instance.getFavoriteJobs("Bearer $token"))
        } catch (ex: Exception) {
            responseHandler.handleException(ex)
        }
    }

    suspend fun addFavoriteJobs(token: String, jobId: String): Resource<Boolean> {
        return try {
            responseHandler.handleSuccess(
                ApiFactory.instance.addFavoriteJobs(
                    "Bearer $token",
                    jobId
                )
            )
        } catch (ex: Exception) {
            responseHandler.handleException(ex)
        }
    }

    suspend fun removeFavoriteJobs(token: String, jobId: String): Resource<Boolean> {
        return try {
            responseHandler.handleSuccess(
                ApiFactory.instance.removeFavoriteJobs(
                    "Bearer $token",
                    jobId
                )
            )
        } catch (ex: Exception) {
            responseHandler.handleException(ex)
        }
    }

    suspend fun updateMyPhoneNumber(token: String, phoneNumber: String): Resource<Nothing> {
        return try {
            responseHandler.handleSuccess(
                ApiFactory.instance.updateMyPhoneNumber(
                    "Bearer $token",
                    phoneNumber
                )
            )
        } catch (ex: Exception) {
            responseHandler.handleException(ex)
        }
    }

    suspend fun updateMyBirthday(token: String, birthday: Date): Resource<Nothing> {
        return try {
            responseHandler.handleSuccess(
                ApiFactory.instance.updateMyBirthday(
                    "Bearer $token",
                    birthday
                )
            )
        } catch (ex: Exception) {
            responseHandler.handleException(ex)
        }
    }

    suspend fun updateMyAddress(
        token: String,
        city: String,
        district: String,
        ward: String,
        houseNumber: String
    ): Resource<Nothing> {
        return try {
            responseHandler.handleSuccess(
                ApiFactory.instance.updateMyAddress(
                    "Bearer $token",
                    city, district, ward, houseNumber
                )
            )
        } catch (ex: Exception) {
            responseHandler.handleException(ex)
        }
    }

    suspend fun updateMySkills(token: String, skills: List<String>): Resource<Nothing> {
        return try {
            responseHandler.handleSuccess(
                ApiFactory.instance.updateMySkills(
                    "Bearer $token",
                    skills
                )
            )
        } catch (ex: Exception) {
            responseHandler.handleException(ex)
        }
    }
}