package com.tutorials.flows.service

import com.tutorials.flows.model.GraphQlRequest
import com.tutorials.flows.model.GraphQlResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/graphql")
//    @Headers("Content-Type: application/json")
    suspend fun getGraphQlResponse(@Body body: GraphQlRequest): GraphQlResponse
}