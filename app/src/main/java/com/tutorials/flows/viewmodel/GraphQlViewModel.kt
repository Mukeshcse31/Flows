package com.tutorials.flows.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutorials.flows.model.GraphQlRequest
import com.tutorials.flows.model.GraphQlResponse
import com.tutorials.flows.model.GraphResponseWithParams
import com.tutorials.flows.service.ApiHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

//https://stackoverflow.com/questions/40875978/how-to-use-graphql-with-retrofit-on-android
// https://amitshekhar.me/blog/retrofit-with-kotlin-flow
class GraphQlViewModel(private val apiHelper: ApiHelper) : ViewModel() {

    val state = MutableStateFlow<GraphQlResponse?>(null)

    val state1 = MutableStateFlow<GraphResponseWithParams?>(null)

    fun getGraphRes() {
        viewModelScope.launch {
            val body = GraphQlRequest(
                "query Query {" +
                        "  country(code: \"BR\") {" +
                        "    name" +
                        "    native" +
                        "    capital" +
                        "    emoji" +
                        "    currency" +
                        "    languages {" +
                        "      code" +
                        "      name" +
                        "    }" +
                        "  }" +
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

    /*
    Backpressure Handling
    Cold Streams
    More Powerful Operators:

    Rich Set of Operators: Flow provides a comprehensive set of operators for transformations (map, filter, flatMap),
    combining flows (merge, zip), and handling errors (catch, retry), offering greater flexibility and control.

    Error Handling:

    Built-In Error Handling: Flow has built-in mechanisms for handling errors gracefully using operators like catch and retry,
    providing robust error management capabilities.

    Reactive Streams Compliance

    Lifecycle Awareness:

    Lifecycle Scoping: While LiveData is lifecycle-aware out of the box,
    Flow can achieve similar behavior using extensions like lifecycleScope and repeatOnLifecycle.

     */

    fun getGraphWithParamsRes() {
        viewModelScope.launch {
//            val map1 = mapOf("characterId" to 11)
            val body = GraphQlRequest(

//                "query ExampleQuery(characterId: ID!, page: Int) {" +
//                        "character(id: \$characterId) {" +
                "query ExampleQuery {" +
                        " character(id: 31) {" +
                        " created" +
                        " species" +
                        " status" +
                        " type" +
                        " }" +
                        "}",
//                emptyMap()
                mapOf("characterId" to 11, "page" to null)
            )
            apiHelper.getGraphWithParamsRes(body)
                .flowOn(Dispatchers.IO)
                .collect {
                    state1.value = it
                    println("GraphQl response viewmodel: $it")
                }
        }
    }
}