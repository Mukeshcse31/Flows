package com.tutorials.flows.model

data class GraphQlRequest(
    val query: String,
    val variables: Map<String, Any>
)

data class GraphQlResponse(
    val data: Country
)
data class Country(

    val country: CountryDetails
)

data class CountryDetails(
    val name: String,
    val native: String,
    val capital: String,
    val currency: String,
    val languages: List<Language>
)
data class Language(
    val code: String,
    val name: String
)