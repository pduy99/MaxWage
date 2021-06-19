package com.helios.maxwage.api

import com.helios.maxwage.models.Job
import com.helios.maxwage.models.JobSchedule
import com.helios.maxwage.models.LoginResponse
import com.helios.maxwage.models.User
import retrofit2.http.*
import java.util.*

/**
 * Created by Helios on 4/9/2021.
 */
interface ApiService {

    /**
     * Auth
     */

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): ApiResponse<LoginResponse>

    /**
     * User
     */

    @GET("users/me")
    suspend fun getMyProfile(@Header("Authorization") accessToken: String): ApiResponse<User>

    @GET("users/me/favorite-jobs")
    suspend fun getFavoriteJobs(@Header("Authorization") accessToken: String): ApiResponse<List<String>>

    @FormUrlEncoded
    @POST("users/me/favorite-jobs")
    suspend fun addFavoriteJobs(
        @Header("Authorization") accessToken: String,
        @Field("jobId") jobId: String
    ): ApiResponse<Boolean>

    @DELETE("users/me/favorite-jobs/{jobId}")
    suspend fun removeFavoriteJobs(
        @Header("Authorization") accessToken: String,
        @Path("jobId") jobId: String
    ): ApiResponse<Boolean>

    @FormUrlEncoded
    @PUT("users/me/phone")
    suspend fun updateMyPhoneNumber(
        @Header("Authorization") accessToken: String,
        @Field("phoneNumber") phoneNumber: String
    ): ApiResponse<Nothing>

    @FormUrlEncoded
    @PUT("users/me/dob")
    suspend fun updateMyBirthday(
        @Header("Authorization") accessToken: String,
        @Field("birthday") birthday: Date
    ): ApiResponse<Nothing>

    @FormUrlEncoded
    @PUT("users/me/address")
    suspend fun updateMyAddress(
        @Header("Authorization") accessToken: String,
        @Field("city") birthday: String,
        @Field("district") district: String,
        @Field("ward") ware: String,
        @Field("houseNumber") houseNumber: String,
    ): ApiResponse<Nothing>

    @FormUrlEncoded
    @PUT("users/me/skills")
    suspend fun updateMySkills(
        @Header("Authorization") accessToken: String,
        @Field("skills") skills: List<String>,
    ): ApiResponse<Nothing>

    /**
     * Jobs
     */

    @GET("jobs")
    suspend fun getAllJobs(@Header("Authorization") accessToken: String): ApiResponse<List<Job>>

    @GET("jobs/{jobId}")
    suspend fun getJobById(
        @Header("Authorization") accessToken: String,
        @Path("jobId") jobId: String
    ): ApiResponse<Job>

    /**
     * Service
     */

    @GET("service/scheduleJob")
    suspend fun scheduleJob(
        @Header("Authorization") accessToken: String,
        @Query("onlyFavoriteJob") onlyFavoriteJob: Boolean,
        @Query("freeTime") freeTime: Array<MutableList<String>>
    ): ApiResponse<JobSchedule>

}