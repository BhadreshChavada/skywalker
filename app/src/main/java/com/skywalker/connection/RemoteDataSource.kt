package com.skywalker.connection

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.skywalker.helper.DataStoreManager
import com.skywalker.helper.DataStoreManager.PreferencesKeys.authToken
import com.skywalker.model.request.*
import com.skywalker.model.respone.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val remoteApiService: RemoteApiService,
    private val dataStoreManager: DataStoreManager
) {

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

    suspend fun getCountries(
        authToken: String, page: Int, perPage: Int
    ): ResultWrapper<CountryData> {
        return safeApiCall {
            remoteApiService.getCountries(createToken(authToken), page, perPage)
        }

    }

    suspend fun getRegions(
        authToken: String, page: Int, perPage: Int
    ): ResultWrapper<RegionResponse> {
        return safeApiCall {
            remoteApiService.getRegions(createToken(authToken), page, perPage)
        }
    }

    suspend fun getPaymentData(
        authToken: String, planPaymentRequest: PlanPaymentRequest
    ): ResultWrapper<StripData> {
        return safeApiCall {
            remoteApiService.getPaymentData(createToken(authToken), planPaymentRequest)
        }
    }

    suspend fun getPlansDetails(
        authToken: String, planId: Int
    ): ResultWrapper<PlanDetailResponse> {
        return safeApiCall {
            remoteApiService.getPlansDetails(createToken(authToken), planId)
        }
    }

    suspend fun updatePaymentStatus(
        authToken: String, paymentStatusRequest: UpdatePaymentStatusRequest
    ): ResultWrapper<SuccessResponse> {
        return safeApiCall {
            remoteApiService.updatePaymentStatus(createToken(authToken), paymentStatusRequest)
        }
    }

    suspend fun getUserData(
        authToken: String
    ): ResultWrapper<LoginResponse> {
        return safeApiCall {
            remoteApiService.getUserDetails(createToken(authToken))
        }
    }

    suspend fun updateUserDetails(
        authToken: String,
        updateProfileRequest: UpdateProfileRequest
    ): ResultWrapper<LoginResponse> {
        return safeApiCall {
            remoteApiService.updateUserDetails(createToken(authToken), updateProfileRequest)
        }
    }

    suspend fun getPlans(
        authToken: String, type: Int, countryId: Int, page: Int, perPage: Int
    ): ResultWrapper<PlanResponse> {
        if (type == 1) {
            return safeApiCall {
                remoteApiService.getPlans(createToken(authToken), type, countryId, page, perPage)
            }
        } else if (type == 2) {
            return safeApiCall {
                remoteApiService.getRegionWisePlans(
                    createToken(authToken),
                    type,
                    countryId,
                    page,
                    perPage
                )
            }
        } else {
            return safeApiCall {
                remoteApiService.getGlobalPlans(
                    createToken(authToken),
                    type,
                    page,
                    perPage
                )
            }
        }

    }

    suspend fun getMyPlans(
        authToken: String, type: Int, page: Int, perPage: Int
    ): ResultWrapper<PlanResponse> {
        return safeApiCall {
            remoteApiService.getMyPlans(createToken(authToken), type, page, perPage)
        }
    }

    suspend fun getOrderHistory(
        authToken: String, page: Int, perPage: Int
    ): ResultWrapper<PlanResponse> {
        return safeApiCall {
            remoteApiService.getOrderHistory(createToken(authToken), page, perPage)
        }
    }


    private fun createToken(token: String): String {
        return "Bearer $token"
    }

}