package com.assignment.myapplication.data

/**
 * A sealed class to handle network error
 */
sealed class NetworkResultState<out R> private constructor() {
    data class Success<out T>(val data: T) : NetworkResultState<T>()
    data class Error(val error: String) : NetworkResultState<Nothing>()
    object Loading : NetworkResultState<Nothing>()
}