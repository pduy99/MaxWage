package com.helios.maxwage.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class User(
    @SerializedName("_id") val id : String,
    @SerializedName("favoriteJobs") val favoriteJobs : List<String>,
    @SerializedName("skils") val skills : List<String>,
    @SerializedName("fullname") val fullName : String,
    @SerializedName("email") val email : String,
    @SerializedName("avatar") val avatar : String,
    @SerializedName("phone") val phone : String,
    @SerializedName("birthday") val birthday : Date,
    @SerializedName("gender") val gender : String,
    @SerializedName("address") val address : String,
)
