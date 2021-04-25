package com.helios.maxwage.api

import com.helios.maxwage.models.Job
import retrofit2.http.GET

/**
 * Created by Helios on 4/9/2021.
 */
interface ApiService {

    /**
     * Jobs
     */

    @GET("jobs")
    suspend fun getAllJobs() : ApiResponse<List<Job>>

}