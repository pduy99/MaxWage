package com.helios.maxwage.repositories

import com.helios.maxwage.api.ApiFactory
import com.helios.maxwage.api.Resource
import com.helios.maxwage.api.ResponseHandler
import com.helios.maxwage.models.JobSchedule

/**
 * Created by Helios on 5/31/2021.
 */

class ServiceRepository(private val responseHandler: ResponseHandler) {

    companion object {
        private var INSTANCE: ServiceRepository? = null
        fun getInstance() = INSTANCE
            ?: ServiceRepository(ResponseHandler()).also {
                INSTANCE = it
            }
    }

    suspend fun scheduleJob(
        token: String,
        freeTime: Array<MutableList<String>>,
        isOnlyFavoriteJob: Boolean,
        matchSkills: Boolean,
        matchAddress: Boolean
    ): Resource<JobSchedule> {
        return try {
            responseHandler.handleSuccess(
                ApiFactory.instance.scheduleJob(
                    "Bearer $token",
                    isOnlyFavoriteJob,
                    freeTime,
                    matchSkills,
                    matchAddress
                )
            )
        } catch (ex: Exception) {
            responseHandler.handleException(ex)
        }
    }
}