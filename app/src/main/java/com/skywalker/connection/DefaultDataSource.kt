package com.skywalker.connection

import com.skywalker.model.request.*
import com.skywalker.model.respone.*
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

    suspend fun getCountries(
        authToken: String, page: Int, perPage: Int
    ): ResultWrapper<CountryData> {
        return requestRemoteDataSource {
            remoteDataSource.getCountries(authToken, page, perPage)
        }
    }

    suspend fun getRegions(
        authToken: String, page: Int, perPage: Int
    ): ResultWrapper<RegionResponse> {
        return requestRemoteDataSource {
            remoteDataSource.getRegions(authToken, page, perPage)
        }
    }

    suspend fun getPlans(
        authToken: String, type: Int, countryId: Int, page: Int, perPage: Int
    ): ResultWrapper<PlanResponse> {
        return requestRemoteDataSource {
            remoteDataSource.getPlans(authToken, type, countryId, page, perPage)
        }
    }
    suspend fun getPaymentData(
        authToken: String, planPaymentRequest: PlanPaymentRequest
    ): ResultWrapper<StripData> {
        return requestRemoteDataSource {
            remoteDataSource.getPaymentData(authToken, planPaymentRequest)
        }
    }

    suspend fun getPlansDetails(
        authToken: String, planId: Int
    ): ResultWrapper<PlanDetailResponse> {
        return requestRemoteDataSource {
            remoteDataSource.getPlansDetails(authToken, planId)
        }
    }

    suspend fun updatePaymentStatus(
        authToken: String, paymentStatusRequest: UpdatePaymentStatusRequest
    ): ResultWrapper<SuccessResponse> {
        return requestRemoteDataSource {
            remoteDataSource.updatePaymentStatus(authToken, paymentStatusRequest)
        }
    }

    suspend fun getUserData(
        authToken: String
    ): ResultWrapper<LoginResponse> {
        return requestRemoteDataSource {
            remoteDataSource.getUserData(authToken)
        }
    }

    suspend fun updateUserDetails(
        authToken: String,
        updateProfileRequest: UpdateProfileRequest
    ): ResultWrapper<LoginResponse> {
        return requestRemoteDataSource {
            remoteDataSource.updateUserDetails(authToken,updateProfileRequest)
        }
    }

    suspend fun getMyPlans(
        authToken: String, type: Int,  page: Int, perPage: Int
    ): ResultWrapper<PlanResponse> {
        return requestRemoteDataSource {
            remoteDataSource.getMyPlans(authToken, type,  page, perPage)
        }
    }
}