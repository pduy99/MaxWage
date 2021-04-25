package com.helios.maxwage.api

import com.google.gson.annotations.SerializedName

class ApiResponse<T> private constructor(
    @SerializedName("data") val data: T?,
    @SerializedName("status_message") val message: String?
) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(data, null)
        }

        fun <T> error(error: String): ApiResponse<T> {
            return ApiResponse(null, error)
        }
    }
}
