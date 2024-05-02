package com.dicoding.asclepius.di

import android.app.Application
import com.dicoding.asclepius.data.local.database.UserHistoryDatabase
import com.dicoding.asclepius.data.remote.api.ApiServiceConfig
import com.dicoding.asclepius.data.remote.api.HealthNewsService
import com.dicoding.asclepius.data.remote.repository.HealthNewsRepository
import com.dicoding.asclepius.data.remote.repository.UserActivityRepository

object ServiceInjector {

    fun provideHealthNewsRepository(): HealthNewsRepository {
        val apiService = createApiService()
        return HealthNewsRepository(apiService)
    }

    fun provideUserActivityRepository(application: Application): UserActivityRepository {
        val userHistoryDataSource = createUserHistoryDataSource(application)
        return UserActivityRepository(userHistoryDataSource)
    }

    private fun createApiService(): HealthNewsService {
        return ApiServiceConfig.getApiService()
    }

    private fun createUserHistoryDataSource(application: Application) =
        UserHistoryDatabase.getDatabaseInstance(application).historyDataSource()
}