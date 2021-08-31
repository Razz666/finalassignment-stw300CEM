package com.rajkumarmagar.locationbasedanythingfinder.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MyServiceBuilder {
//    const val BASE_URL = "http://10.0.2.2:90/"
    const val BASE_URL = "http://192.168.137.147:90/"
    var token: String? = null

    private val okHttpClient = OkHttpClient.Builder()
    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient.build())

    private val retrofit = retrofitBuilder.build()

    fun <T> buildService(serviceType: Class<T>): T {
        return retrofit.create(serviceType)
    }
}