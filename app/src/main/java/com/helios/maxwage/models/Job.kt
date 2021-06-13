package com.helios.maxwage.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Helios on 4/9/2021.
 */

data class Job(
    @SerializedName("isFlexible") val isFlexible: Boolean,
    @SerializedName("description") val description: String,
    //@SerializedName("schedule") val schedule : List<Schedule>,
    @SerializedName("skills") val skills: List<String>,
    @SerializedName("_id") val _id: String,
    @SerializedName("name") val name: String,
    @SerializedName("salary") val salary: Int,
    @SerializedName("address") val address: String,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("company") val company: String
)