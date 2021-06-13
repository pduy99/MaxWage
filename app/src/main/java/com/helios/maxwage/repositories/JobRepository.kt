package com.helios.maxwage.repositories

import com.helios.maxwage.api.ApiFactory
import com.helios.maxwage.api.Resource
import com.helios.maxwage.api.ResponseHandler
import com.helios.maxwage.models.Job

/**
 * Created by Helios on 4/9/2021.
 */
class JobRepository(private val responseHandler: ResponseHandler) {

    companion object {
        private var INSTANCE: JobRepository? = null
        fun getInstance() = INSTANCE
            ?: JobRepository(ResponseHandler()).also {
                INSTANCE = it
            }
    }

    suspend fun getAllJobs(accessToken: String): Resource<List<Job>> {
        return try {
            responseHandler.handleSuccess(ApiFactory.instance.getAllJobs("Bearer $accessToken"))
        } catch (ex: Exception) {
            responseHandler.handleException(ex)
        }
    }

    suspend fun getJobById(accessToken: String, jobId: String): Resource<Job> {
        return try {
            responseHandler.handleSuccess(
                ApiFactory.instance.getJobById(
                    "Bearer $accessToken",
                    jobId
                )
            )
        } catch (ex: Exception) {
            responseHandler.handleException(ex)
        }
    }
}