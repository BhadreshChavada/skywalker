package com.skywalker.connection

import com.skywalker.model.request.*
import com.skywalker.model.respone.*
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

    @GET("plans")
    suspend fun getPlans(
        @Header("Authorization") authHeader: String,
        @Query("type") type: Int = 1,
        @Query("countryId") countryId: Int,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Response<PlanResponse>

    @GET("plans")
    suspend fun getRegionWisePlans(
        @Header("Authorization") authHeader: String,
        @Query("type") type: Int = 1,
        @Query("regionId") countryId: Int,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Response<PlanResponse>

    @GET("plans")
    suspend fun getGlobalPlans(
        @Header("Authorization") authHeader: String,
        @Query("type") type: Int = 1,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Response<PlanResponse>

    @POST("orders")
    suspend fun getPaymentData(
        @Header("Authorization") authHeader: String,
        @Body planPaymentRequest: PlanPaymentRequest
    ): Response<StripData>

    @GET("plans/{id}")
    suspend fun getPlansDetails(
        @Header("Authorization") authHeader: String,
        @Path("id") plaId:Int
    ): Response<PlanDetailResponse>

    @GET("users")
    suspend fun getUserDetails(
        @Header("Authorization") authHeader: String
    ): Response<LoginResponse>

    @PUT("users")
    suspend fun updateUserDetails(
        @Header("Authorization") authHeader: String,
        @Body updateProfileRequest: UpdateProfileRequest
    ): Response<LoginResponse>

    @PUT("orders/verify")
    suspend fun updatePaymentStatus(
        @Header("Authorization") authHeader: String,
        @Body paymentStatusRequest: UpdatePaymentStatusRequest
    ): Response<SuccessResponse>

    @GET("plans/my")
    suspend fun getMyPlans(
        @Header("Authorization") authHeader: String,
        @Query("type") type: Int = 1,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Response<PlanResponse>

    @GET("orders/history")
    suspend fun getOrderHistory(
        @Header("Authorization") authHeader: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Response<PlanResponse>
}