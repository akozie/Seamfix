package com.seamfix.seamfix.data.remote.apiServices

import com.seamfix.seamfix.data.remote.apiResponses.SOSResponse
import com.seamfix.seamfix.data.remote.dto.SOSRequestDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SOSApiService {

    @POST("create")
    suspend fun sendSOSRequest(@Body sosRequestDTO: SOSRequestDTO): Response<SOSResponse>

}