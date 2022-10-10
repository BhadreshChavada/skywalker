package com.skywalker.connection

import com.skywalker.model.request.LoginRequest
import com.skywalker.model.request.SignupRequest
import com.skywalker.model.respone.LoginResponse
import com.skywalker.model.respone.SuccessResponse
import javax.inject.Inject

class DefaultDataSource
@Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val connectivityStatusProvider: ConnectivityStatusProvider
) {

    private suspend fun <T> requestRemoteDataSource(
        remoteRequest: suspend () -> ResultWrapper<T>
    ): ResultWrapper<T> {
        return if (!connectivityStatusProvider.isInternetAvailable())
            ResultWrapper.Error(exception = NoInternetException())
        else
            remoteRequest.invoke()
    }

    suspend fun doRegisterWithEmail(
        requestModel: SignupRequest
    ): ResultWrapper<SuccessResponse> {
        return requestRemoteDataSource {
            remoteDataSource.doRegisterWithEmail(requestModel)
        }
    }

    suspend fun doLoginWithEmail(
        requestModel: LoginRequest
    ): ResultWrapper<LoginResponse> {
        return requestRemoteDataSource {
            remoteDataSource.doLoginWithEmail(requestModel)
        }
    }
}