package com.seamfix.seamfix.utils

import com.seamfix.seamfix.presentation.viewState.Resource
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class NetworkUtils() {
    fun <T> getServerResponse(serverResponse: Response<T>): Resource<T?> {
        return when {
            serverResponse.code() in 200..299 -> {
                Resource.Success(serverResponse.body()!!)
            }

            serverResponse.code() in 400..499 -> {
                Resource.Error("Client Error")
            }

            serverResponse.code() >= 500 -> {
                Resource.Error("Server Error")
            }

            else -> {
                Resource.Error("Error")
            }
        }
    }
}