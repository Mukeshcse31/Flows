package com.tutorials.flows.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutorials.flows.model.GraphQlRequest
import com.tutorials.flows.model.GraphQlResponse
import com.tutorials.flows.service.ApiHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

//https://stackoverflow.com/questions/40875978/how-to-use-graphql-with-retrofit-on-android
// https://amitshekhar.me/blog/retrofit-with-kotlin-flow
class GraphQlViewModel(val apiHelper: ApiHelper) : ViewModel() {

//    private val _data: MutableLiveData<GraphQlResponse> = MutableLiveData(null)
//    val data: LiveData<GraphQlResponse?> = _data

    val state = MutableStateFlow<GraphQlResponse?>(null)

    fun getGraphRes() {
        viewModelScope.launch {
            val body = GraphQlRequest(
                "query Query {\n" +
                        "  country(code: \"BR\") {\n" +
                        "    name\n" +
                        "    native\n" +
                        "    capital\n" +
                        "    emoji\n" +
                        "    currency\n" +
                        "    languages {\n" +
                        "      code\n" +
                        "      name\n" +
                        "    }\n" +
                        "  }\n" +
                        "}",
                emptyMap()
            )
            apiHelper.getGraphQlResponse(body)
                .flowOn(Dispatchers.IO)
                .collect {
                    state.value = it
                    println("GraphQl response viewmodel: $it")
                }
        }
    }
}