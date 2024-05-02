package com.dicoding.asclepius.utils

sealed class ResultHandler<out R> private constructor() {
    data class Success<out T>(val data: T) : ResultHandler<T>()
    data class Error(val error: String) : ResultHandler<Nothing>()
    data object Loading : ResultHandler<Nothing>()
}