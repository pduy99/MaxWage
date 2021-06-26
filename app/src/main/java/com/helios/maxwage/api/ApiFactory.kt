package com.helios.maxwage.api

import com.google.gson.GsonBuilder
import com.helios.maxwage.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Helios on 4/9/2021.
 */

object ApiFactory {

    val instance: ApiService = Retrofit.Builder().run {
        val gson = GsonBuilder()
            .enableComplexMapKeySerialization()
            .setPrettyPrinting()
            .create()

        baseUrl(BuildConfig.URL_SERVER)
        addConverterFactory(GsonConverterFactory.create())
        client(createRequestInterceptorClient())
        build()
    }.create(ApiService::class.java)

    private fun createRequestInterceptorClient(): OkHttpClient {
        val interceptor = Interceptor { chain ->
            val origin = chain.request()
            val requestBuilder = origin.newBuilder()
            val request = requestBuilder.build()
            chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .connectTimeout(20.toLong(), TimeUnit.SECONDS)
            .readTimeout(20.toLong(), TimeUnit.SECONDS)
            .writeTimeout(20.toLong(), TimeUnit.SECONDS)
            .build()
    }
}