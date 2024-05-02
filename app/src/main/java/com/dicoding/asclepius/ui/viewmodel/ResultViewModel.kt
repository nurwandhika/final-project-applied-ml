package com.dicoding.asclepius.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.model.NewsItem
import com.dicoding.asclepius.data.model.NewsResponse
import com.dicoding.asclepius.data.remote.repository.HealthNewsRepository
import com.dicoding.asclepius.di.ServiceInjector
import com.dicoding.asclepius.utils.ResultHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultViewModel(private val newsRepository: HealthNewsRepository) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _newsList = MutableLiveData<List<NewsItem>>(arrayListOf())
    val articleList: LiveData<List<NewsItem>> = _newsList

    init {
        fetchNewsArticles()
    }

    private fun fetchNewsArticles() {
        launchFetchNewsArticles()
    }

    private fun launchFetchNewsArticles() {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.fetchHealthNews().collect { result ->
                handleResult(result)
            }
        }
    }

    private fun handleResult(result: ResultHandler<NewsResponse>) {
        when (result) {
            is ResultHandler.Loading -> updateLoadingState(true)
            is ResultHandler.Success -> handleSuccessResult(result.data.newsList)
            is ResultHandler.Error -> handleFailureResult()
        }
    }

    private fun handleSuccessResult(newsItems: List<NewsItem>) {
        updateLoadingState(false)
        updateNewsList(newsItems)
    }

    private fun handleFailureResult() {
        updateLoadingState(false)
    }

    private fun updateLoadingState(isLoading: Boolean) {
        _isLoading.postValue(isLoading)
    }

    private fun updateNewsList(newsItems: List<NewsItem>) {
        _newsList.postValue(newsItems)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return createResultViewModel() as T
        }

        private fun createResultViewModel(): ResultViewModel {
            val newsRepository = provideHealthNewsRepository()
            return ResultViewModel(newsRepository)
        }

        private fun provideHealthNewsRepository(): HealthNewsRepository {
            return ServiceInjector.provideHealthNewsRepository()
        }
    }
}