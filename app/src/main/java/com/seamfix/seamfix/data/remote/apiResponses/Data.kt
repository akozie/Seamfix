package com.seamfix.seamfix.data.remote.apiResponses

data class Data(
    val id: Int,
    val image: String,
    val location: Location,
    val phoneNumbers: List<String>
)