package com.tutorials.flows

import com.tutorials.flows.service.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Util {

    val retrofit = Retrofit.Builder()
    .baseUrl(Constants.BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}