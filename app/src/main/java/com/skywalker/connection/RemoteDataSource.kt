package com.skywalker.connection

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.skywalker.model.request.LoginRequest
import com.skywalker.model.request.SignupRequest
import com.skywalker.model.respone.ErrorResponse
import com.skywalker.model.respone.LoginResponse
import com.skywalker.model.respone.SuccessResponse
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val remoteApiService: RemoteApiService
) {

    private suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ResultWrapper<T> =
        try {
            val response: Response<T> = apiCall()
            if (response.isSuccessful) {
                ResultWrapper.Success(response.body())
            } else {

                val gson = Gson()
                val type = object : TypeToken<ErrorResponse>() {}.type
                var errorResponse: ErrorResponse? =
                    gson.fromJson(response.errorBody()!!.charStream(), type)
                Log.d("errorResponse", errorResponse?.message!!)
                ResultWrapper.Error(exception = HttpException(response), errorResponse = errorResponse)
            }
        } catch (httpException: HttpException) {
            ResultWrapper.Error(exception = httpException)
        } catch (e: Exception) {
            ResultWrapper.Error(exception = e)
        }

    suspend fun doRegisterWithEmail(
        signupRequest: SignupRequest
    ): ResultWrapper<SuccessResponse> {
        return safeApiCall {
            remoteApiService.doRegisterWithEmail(signupRequest)
        }
    }

    suspend fun doLoginWithEmail(
        loginRequestModel: LoginRequest
    ): ResultWrapper<LoginResponse> {
        return safeApiCall {
            remoteApiService.doLoginWithEmail(loginRequestModel)
        }
    }

}