package com.tutorials.flows.service

import com.tutorials.flows.model.GraphQlRequest
import com.tutorials.flows.model.GraphQlResponse
import kotlinx.coroutines.flow.Flow

interface ApiHelper {

    fun getGraphQlResponse(body: GraphQlRequest): Flow<GraphQlResponse>

}