package com.helios.maxwage.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Helios on 6/4/2021.
 */

data class JobSchedules(
    @SerializedName("combos") val combos: List<List<List<TimeCell>>>,
    @SerializedName("salary") val salary: Int
)

data class TimeCell(
    @SerializedName("time") val time: String,
    @SerializedName("ids") var jobIds: List<String>
)