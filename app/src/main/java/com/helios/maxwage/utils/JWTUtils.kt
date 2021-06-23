package com.helios.maxwage.utils

import com.auth0.android.jwt.Claim
import com.auth0.android.jwt.JWT
import com.helios.maxwage.sharepreferences.SharedPrefs

/**
 * Created by Helios on 6/2/2021.
 */
object JWTUtils {
    fun checkExpired(): Boolean {
        if (SharedPrefs.accessToken.isNotEmpty()) {
            val jwt = JWT(SharedPrefs.accessToken)
            return jwt.isExpired(0)
        }
        return true
    }

    fun parseId(): String? {
        return try {
            val token = SharedPrefs.accessToken
            val jwt = token.let { JWT(it) }
            val subscriptionMetaData: Claim = jwt.getClaim("_id")
            subscriptionMetaData.asString()!!
        } catch (ex: Exception) {
            null
        }
    }
}