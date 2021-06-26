package com.helios.maxwage.interfaces

/**
 * Created by Helios on 6/25/2021.
 */
interface OnJobClick {
    fun onJobClick(jobId: String, isFavorite: Boolean)
}