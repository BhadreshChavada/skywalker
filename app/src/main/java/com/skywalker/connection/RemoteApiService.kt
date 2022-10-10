package com.skywalker.connection

import com.skywalker.model.request.LoginRequest
import com.skywalker.model.request.SignupRequest
import com.skywalker.model.respone.LoginResponse
import com.skywalker.model.respone.SuccessResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface RemoteApiService {

    @POST("auth/register")
    suspend fun doRegisterWithEmail(
        @Body signupRequest: SignupRequest
    ): Response<SuccessResponse>

    @POST("auth/login")
    suspend fun doLoginWithEmail(
        @Body loginWithEmailRequest: LoginRequest
    ): Response<LoginResponse>
}