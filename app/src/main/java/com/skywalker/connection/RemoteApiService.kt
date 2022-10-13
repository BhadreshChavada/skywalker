package com.skywalker.connection

import com.skywalker.model.request.LoginRequest
import com.skywalker.model.request.SignupRequest
import com.skywalker.model.respone.CountryData
import com.skywalker.model.respone.LoginResponse
import com.skywalker.model.respone.RegionResponse
import com.skywalker.model.respone.SuccessResponse
import retrofit2.Response
import retrofit2.http.*

interface RemoteApiService {

    @POST("auth/register")
    suspend fun doRegisterWithEmail(
        @Body signupRequest: SignupRequest
    ): Response<SuccessResponse>

    @POST("auth/login")
    suspend fun doLoginWithEmail(
        @Body loginWithEmailRequest: LoginRequest
    ): Response<LoginResponse>

    @GET("plans/countries")
    suspend fun getCountries(
        @Header("Authorization") authHeader: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Response<CountryData>

    @GET("plans/regions")
    suspend fun getRegions(
        @Header("Authorization") authHeader: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Response<RegionResponse>
}