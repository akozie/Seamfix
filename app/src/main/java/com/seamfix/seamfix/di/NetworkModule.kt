package com.seamfix.seamfix.di

import com.seamfix.seamfix.data.remote.apiServices.SOSApiService
import com.seamfix.seamfix.utils.AppConstants.BASE_URL
import com.seamfix.seamfix.utils.AppConstants.STRING_LOGGING_INTERCEPTOR_TAG
import com.seamfix.seamfix.utils.AppConstants.TIME_OUT_15
import com.seamfix.seamfix.utils.NetworkUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesBaseUrl(): String = BASE_URL

    @Singleton
    @Provides
    @Named(STRING_LOGGING_INTERCEPTOR_TAG)
    fun providesLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun providesOKHTTPClient(
        @Named(STRING_LOGGING_INTERCEPTOR_TAG) loggingInterceptor: Interceptor,
    ): OkHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(TIME_OUT_15, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT_15, TimeUnit.SECONDS)
        .writeTimeout(TIME_OUT_15, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .addInterceptor(loggingInterceptor)
        .build()

    @Singleton
    @Provides
    fun providesRetrofitForNetworkCall(
        okHttpClient: OkHttpClient,
        baseUrl: String,
    ): Retrofit =
        Retrofit.Builder().baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun providesSOSApiService(
        retrofit: Retrofit
    ): SOSApiService = retrofit.create(SOSApiService::class.java)

    @Singleton
    @Provides
    fun providesNetworkUtil(): NetworkUtils = NetworkUtils()
}