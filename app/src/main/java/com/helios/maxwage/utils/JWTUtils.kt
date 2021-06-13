package com.helios.maxwage.utils

import com.auth0.android.jwt.Claim
import com.auth0.android.jwt.JWT
import com.helios.maxwage.sharepreferences.SharedPrefs

/**
 * Created by Helios on 6/2/2021.
 */
object JWTUtils {
    fun checkExpired(token: String): Boolean {
        val jwt = JWT(token)
        return jwt.isExpired(0)
    }

    fun parseId(): String? {
        return try {
            val token = SharedPrefs.accessToken
            val jwt = token?.let { JWT(it) }
            val subscriptionMetaData: Claim? = jwt?.getClaim("_id")
            subscriptionMetaData?.asString()!!
        } catch (ex: Exception) {
            null
        }
    }
}