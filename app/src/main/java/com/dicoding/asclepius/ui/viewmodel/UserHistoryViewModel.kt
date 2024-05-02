package com.dicoding.asclepius.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.data.remote.repository.UserActivityRepository
import com.dicoding.asclepius.di.ServiceInjector

class UserHistoryViewModel(private val historyRepository: UserActivityRepository) : ViewModel() {
    fun getAllHistories() = fetchAllHistoryEntries()

    private fun fetchAllHistoryEntries() = executeFetchAllHistoryEntries()

    private fun executeFetchAllHistoryEntries() = historyRepository.fetchAllActivityEntries()

    @Suppress("UNCHECKED_CAST")
    class Factory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return createViewModel() as T
        }

        private fun createViewModel(): UserHistoryViewModel {
            val historyRepository = provideUserActivityRepository()
            return UserHistoryViewModel(historyRepository)
        }

        private fun provideUserActivityRepository(): UserActivityRepository {
            return ServiceInjector.provideUserActivityRepository(application)
        }
    }
}