package com.ua.historicalsitesapp.util

/**
 * A generic class that holds a value with its loading status.
 *
 * @param <T>
 */
sealed class LoginResult<out T : Any> {

  data class Success<out T : Any>(val data: T) : LoginResult<T>()

  sealed class Error : LoginResult<Nothing>() {
    data class ExceptionError(val exception: Exception) : Error()

    data class MessageError(val message: String) : Error()
  }

  override fun toString(): String =
      when (this) {
        is Success<*> -> "Success[data=$data]"
        is Error.ExceptionError -> "Error[exception=${exception.message}]"
        is Error.MessageError -> "Error[message=$message]"
      }
}
