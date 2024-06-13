package com.seamfix.seamfix.data.remote.dto

data class SOSRequestDTO(
    val image: String,
    val location: LocationDTO,
    val phoneNumbers: List<String>
)