package com.dicoding.asclepius.data.remote.repository

import com.dicoding.asclepius.data.remote.api.HealthNewsService
import com.dicoding.asclepius.utils.ResultHandler
import kotlinx.coroutines.flow.flow

class HealthNewsRepository(private val newsHealthNewsService: HealthNewsService) {
    fun fetchHealthNews() = flow {
        emit(ResultHandler.Loading)
        try {
            val healthNewsResponse = newsHealthNewsService.getHealthRelatedArticles()
            emit(ResultHandler.Success(healthNewsResponse))
        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(ResultHandler.Error(exception.message.toString()))
        }
    }
}