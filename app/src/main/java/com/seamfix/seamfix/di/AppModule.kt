package com.seamfix.seamfix.di

import com.seamfix.seamfix.domain.repository.Repository
import com.seamfix.seamfix.domain.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesRepository(repositoryImpl: RepositoryImpl): Repository = repositoryImpl
}