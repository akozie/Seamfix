package com.seamfix.seamfix.domain.repository

import com.seamfix.seamfix.presentation.viewState.Resource
import com.seamfix.seamfix.data.remote.apiResponses.SOSResponse
import com.seamfix.seamfix.data.remote.dto.SOSRequestDTO
import retrofit2.http.Body

interface Repository {
    suspend fun sendSOSRequest(@Body sosRequestDTO: SOSRequestDTO): Resource<SOSResponse?>
}