package com.tutorials.flows

import com.google.gson.Gson
import com.tutorials.flows.service.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Util {
//    Gson()

    val gson = Gson().newBuilder().setDateFormat("YYYYMM").create()

    private fun getRetrofit(url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun getService(url: String): ApiService {
       return getRetrofit(url).create(ApiService::class.java)
    }
}