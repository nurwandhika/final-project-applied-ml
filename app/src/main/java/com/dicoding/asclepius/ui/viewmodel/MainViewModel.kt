package com.dicoding.asclepius.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.model.UserActivity
import com.dicoding.asclepius.data.remote.repository.UserActivityRepository
import com.dicoding.asclepius.di.ServiceInjector
import kotlinx.coroutines.launch

class MainViewModel(private val historyRepository: UserActivityRepository) : ViewModel() {
    fun insertHistory(userActivity: UserActivity) {
        addHistoryEntry(userActivity)
    }

    private fun addHistoryEntry(userActivity: UserActivity) {
        launchAddHistoryEntry(userActivity)
    }

    private fun launchAddHistoryEntry(userActivity: UserActivity) {
        viewModelScope.launch {
            historyRepository.addActivityEntry(userActivity)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return createMainViewModel() as T
        }

        private fun createMainViewModel(): MainViewModel {
            val historyRepository = provideUserActivityRepository()
            return MainViewModel(historyRepository)
        }

        private fun provideUserActivityRepository(): UserActivityRepository {
            return ServiceInjector.provideUserActivityRepository(application)
        }
    }
}