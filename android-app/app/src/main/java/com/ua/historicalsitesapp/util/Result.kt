package com.ua.historicalsitesapp.util

/**
 * A generic class that holds a value with its loading status.
 *
 * @param <T>
 */
sealed class Result<out T : Any, out U : Any> {
  data class Success<out T : Any>(val data: T) : Result<T>()

  data class ErrorLockout(val message: String) : Result<String>()

  data class Error(val exception: Exception) : Result<Nothing>()

  override fun toString(): String {
    return when (this) {
      is Success<*> -> "Success[data=$data]"
      is Error -> "Error[exception=$exception]"
      is ErrorLockout -> "ErrorLockout[message=$message]"
    }
  }
}
