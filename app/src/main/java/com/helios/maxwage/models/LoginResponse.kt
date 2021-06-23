package com.helios.maxwage.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Helios on 4/27/2021.
 */
data class LoginResponse(
    @SerializedName("_id") val _id: String,
    @SerializedName("token") val token: String
)