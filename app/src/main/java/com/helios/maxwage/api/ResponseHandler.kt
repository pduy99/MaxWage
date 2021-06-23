package com.helios.maxwage.api

import android.util.Log
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketTimeoutException

open class ResponseHandler {

    fun <T : Any> handleSuccess(data: ApiResponse<T>?): Resource<T> {
        return Resource.success(data?.data, data?.message)
    }

    fun <T : Any> handleException(e: Exception): Resource<T> {
        Log.d(TAG, e.toString())
        return when (e) {
            is HttpException -> {
                Resource.error(parseErrorBody(e) ?: getDefaultErrorMessage(e.code()))
            }
            is SocketTimeoutException -> {
                Resource.error("Connect timeout")
            }
            else -> {
                Resource.error(e.message ?: e.toString())
            }
        }
    }

    private fun getDefaultErrorMessage(code: Int): String {
        return when (code) {
            401 -> "Error, Unauthorised!"
            404 -> "Error, Not found!"
            403 -> "Error, Forbidden access!"
            502 -> "Error, Bad Gateway do server free cùi mía :((("
            409 -> "Error, already existed"
            else -> "Something went wrong"
        }
    }

    private fun parseErrorBody(ex: HttpException): String? {
        return try {
            val jObjError = JSONObject(ex.response()?.errorBody()!!.string())
            val message = jObjError.getString("status_message")
            if (message != "") {
                message
            } else {
                null
            }
        } catch (ex: Exception) {
            null
        }
    }

    companion object {
        const val TAG = "ResponseHandler"
    }
}