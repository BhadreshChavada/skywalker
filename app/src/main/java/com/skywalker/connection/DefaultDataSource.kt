package com.skywalker.connection

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.skywalker.model.request.*
import com.skywalker.model.respone.*
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class DefaultDataSource
@Inject constructor(
    private val connectivityStatusProvider: ConnectivityStatusProvider,
    private val remoteApiService: RemoteApiService,
    private val hotspotApiService: HotspotApiService,
) {

    private suspend fun <T> requestRemoteDataSource(
        remoteRequest: suspend () -> ResultWrapper<T>
    ): ResultWrapper<T> {
        return if (!connectivityStatusProvider.isInternetAvailable())
            ResultWrapper.Error(exception = NoInternetException())
        else
            remoteRequest.invoke()
    }

    private suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ResultWrapper<T> =
        try {
            val response: Response<T> = apiCall()
            if (response.isSuccessful) {
                ResultWrapper.Success(response.body())
            } else if (response.code() == 401) {
                ResultWrapper.SessionExpired(response.code())
            } else {

                val gson = Gson()
                val type = object : TypeToken<ErrorResponse>() {}.type
                val errorResponse: ErrorResponse? =
                    gson.fromJson(response.errorBody()!!.charStream(), type)
                Log.d("errorResponse", errorResponse?.message!!)
                ResultWrapper.Error(
                    exception = HttpException(response),
                    errorResponse = errorResponse
                )
            }
        } catch (httpException: HttpException) {
            ResultWrapper.Error(exception = httpException)
        } catch (e: Exception) {
            ResultWrapper.Error(exception = e)
        }

    private fun createToken(token: String): String {
        return "Bearer $token"
    }

    suspend fun doRegisterWithEmail(
        requestModel: SignupRequest
    ): ResultWrapper<SuccessResponse> {
        return requestRemoteDataSource {
            safeApiCall {
                if (requestModel.referralCode.isEmpty()) {
                    val req = SignupRequestWithoutReferal(
                        requestModel.password,
                        requestModel.userName,
                        requestModel.email
                    )
                    remoteApiService.doRegisterWithEmail(req)
                } else {
                    remoteApiService.doRegisterWithEmail(requestModel)
                }

            }
        }
    }

    suspend fun doLoginWithEmail(
        requestModel: LoginRequest
    ): ResultWrapper<LoginResponse> {
        return requestRemoteDataSource {
            safeApiCall {
                remoteApiService.doLoginWithEmail(requestModel)
            }
        }
    }

    suspend fun doSocialSignUp(
        requestModel: SocialLoginRequest
    ): ResultWrapper<LoginResponse> {
        return requestRemoteDataSource {
            safeApiCall {
                remoteApiService.doSocialSignUp(requestModel)
            }
        }
    }

    suspend fun getCountries(
        authToken: String, page: Int, perPage: Int
    ): ResultWrapper<CountryData> {
        return requestRemoteDataSource {
            safeApiCall {
                remoteApiService.getCountries(createToken(authToken), page, perPage)
            }
        }
    }

    suspend fun getRegions(
        authToken: String, page: Int, perPage: Int
    ): ResultWrapper<RegionResponse> {
        return requestRemoteDataSource {
            safeApiCall {
                remoteApiService.getRegions(createToken(authToken), page, perPage)
            }
        }
    }

    suspend fun getPlans(
        authToken: String, type: Int, countryId: Int, page: Int, perPage: Int
    ): ResultWrapper<PlanResponse> {
        return requestRemoteDataSource {
            when (type) {
                1 -> {
                    safeApiCall {
                        remoteApiService.getPlans(
                            createToken(authToken),
                            type,
                            countryId,
                            page,
                            perPage
                        )
                    }
                }
                2 -> {
                    safeApiCall {
                        remoteApiService.getRegionWisePlans(
                            createToken(authToken),
                            type,
                            countryId,
                            page,
                            perPage
                        )
                    }
                }
                else -> {
                    safeApiCall {
                        remoteApiService.getGlobalPlans(
                            createToken(authToken),
                            type,
                            page,
                            perPage
                        )
                    }
                }
            }

        }
    }

    suspend fun getPaymentData(
        authToken: String, planPaymentRequest: PlanPaymentRequest
    ): ResultWrapper<StripData> {
        return requestRemoteDataSource {
            safeApiCall {
                remoteApiService.getPaymentData(createToken(authToken), planPaymentRequest)
            }
        }
    }

    suspend fun getPlansDetails(
        authToken: String, planId: Int
    ): ResultWrapper<PlanDetailResponse> {
        return requestRemoteDataSource {
            safeApiCall {
                remoteApiService.getPlansDetails(createToken(authToken), planId)
            }
        }
    }

    suspend fun updatePaymentStatus(
        authToken: String, paymentStatusRequest: UpdatePaymentStatusRequest
    ): ResultWrapper<SuccessResponse> {
        return requestRemoteDataSource {
            safeApiCall {
                remoteApiService.updatePaymentStatus(createToken(authToken), paymentStatusRequest)
            }
        }
    }

    suspend fun getUserData(
        authToken: String
    ): ResultWrapper<LoginResponse> {
        return requestRemoteDataSource {
            safeApiCall {
                remoteApiService.getUserDetails(createToken(authToken))
            }
        }
    }

    suspend fun updateUserDetails(
        authToken: String,
        updateProfileRequest: UpdateProfileRequest
    ): ResultWrapper<LoginResponse> {
        return requestRemoteDataSource {
            safeApiCall {
                remoteApiService.updateUserDetails(createToken(authToken), updateProfileRequest)
            }
        }
    }

    suspend fun getMyPlans(
        authToken: String, type: Int, page: Int, perPage: Int
    ): ResultWrapper<PlanResponse> {
        return requestRemoteDataSource {
            safeApiCall {
                remoteApiService.getMyPlans(createToken(authToken), type, page, perPage)
            }
        }
    }

    suspend fun getOrderHistory(
        authToken: String, page: Int, perPage: Int
    ): ResultWrapper<PlanResponse> {
        return requestRemoteDataSource {
            safeApiCall {
                remoteApiService.getOrderHistory(createToken(authToken), page, perPage)
            }
        }
    }

    suspend fun getHotSpotData(): ResultWrapper<List<HotspotDetails>> {
        return requestRemoteDataSource {
            safeApiCall {
                hotspotApiService.getHotSpotData()
            }
        }
    }

}