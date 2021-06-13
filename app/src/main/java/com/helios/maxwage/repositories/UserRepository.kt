package com.helios.maxwage.repositories

import com.helios.maxwage.api.ApiFactory
import com.helios.maxwage.api.Resource
import com.helios.maxwage.api.ResponseHandler
import com.helios.maxwage.models.LoginResponse

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
}