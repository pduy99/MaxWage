package com.helios.maxwage.api

data class Resource<out T>(val status: ApiStatus, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?, message: String?): Resource<T> {
            return Resource(ApiStatus.SUCCESS, data, message)
        }

        fun <T> error(msg: String): Resource<T> {
            return Resource(ApiStatus.ERROR, null, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(ApiStatus.LOADING, data, null)
        }
    }
}
