package com.skywalker.connection

import com.skywalker.model.respone.ErrorResponse

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T?) : ResultWrapper<T>()
    data class Error(val code: Int? = null, val exception: Throwable, val errorResponse: ErrorResponse? = null) : ResultWrapper<Nothing>()
}