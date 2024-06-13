package com.seamfix.seamfix.domain.repository

import com.seamfix.seamfix.presentation.viewState.Resource
import com.seamfix.seamfix.data.remote.apiResponses.SOSResponse
import com.seamfix.seamfix.data.remote.apiServices.SOSApiService
import com.seamfix.seamfix.utils.NetworkUtils
import com.seamfix.seamfix.data.remote.dto.SOSRequestDTO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val sosApiService: SOSApiService,
    private val networkUtils: NetworkUtils
) : Repository {

    override suspend fun sendSOSRequest(sosRequestDTO: SOSRequestDTO): Resource<SOSResponse?> =
        sosApiService.sendSOSRequest(sosRequestDTO).let {
            networkUtils.getServerResponse(it)
        }

}