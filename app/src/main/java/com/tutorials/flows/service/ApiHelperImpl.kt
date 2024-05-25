package com.tutorials.flows.service

import com.tutorials.flows.model.GraphQlRequest
import com.tutorials.flows.model.GraphQlResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class ApiHelperImpl(val apiService: ApiService): ApiHelper {
    override fun getGraphQlResponse(body: GraphQlRequest): Flow<GraphQlResponse> {
        return flow {
            emit(apiService.getGraphQlResponse(body))
        }
    }
}