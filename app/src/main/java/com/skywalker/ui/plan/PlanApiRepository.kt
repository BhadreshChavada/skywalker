package com.skywalker.ui.plan

import androidx.lifecycle.MutableLiveData
import com.skywalker.connection.DefaultDataSource
import com.skywalker.connection.ResultWrapper
import com.skywalker.model.request.LoginRequest
import com.skywalker.model.request.PlanPaymentRequest
import com.skywalker.model.request.SignupRequest
import com.skywalker.model.request.UpdatePaymentStatusRequest
import com.skywalker.model.respone.*
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

@Module
@InstallIn(ActivityComponent::class)
class PlanApiRepository @Inject constructor(
    private val defaultDataSource: DefaultDataSource,
) {

    private val _planLiveData = MutableLiveData<ResultWrapper<PlanResponse>?>()
    val planLiveData: MutableLiveData<ResultWrapper<PlanResponse>?>
        get() = _planLiveData

    private val _stripLiveData = MutableLiveData<ResultWrapper<StripData>?>()
    val stripLiveData: MutableLiveData<ResultWrapper<StripData>?>
        get() = _stripLiveData

    private val _planDetailsLiveData = MutableLiveData<ResultWrapper<PlanDetailResponse>?>()
    val planDetailsLiveData: MutableLiveData<ResultWrapper<PlanDetailResponse>?>
        get() = _planDetailsLiveData

    private val _paymentStatusLiveData = MutableLiveData<ResultWrapper<SuccessResponse>?>()
    val paymentStatusLiveData: MutableLiveData<ResultWrapper<SuccessResponse>?>
        get() = _paymentStatusLiveData

    private val _currentSimLiveData = MutableLiveData<ResultWrapper<PlanResponse>?>()
    val currentSimLiveData: MutableLiveData<ResultWrapper<PlanResponse>?>
        get() = _currentSimLiveData

    private val _activatedSimLiveData = MutableLiveData<ResultWrapper<PlanResponse>?>()
    val activatedSimLiveData: MutableLiveData<ResultWrapper<PlanResponse>?>
        get() = _activatedSimLiveData

    suspend fun getPlans(authToken: String, type: Int, countryId: Int, page: Int, perPage: Int) {
        _planLiveData.value = defaultDataSource.getPlans(authToken, type, countryId, page, perPage)
        _planLiveData.value = null
    }

    suspend fun getPaymentData(authToken: String, planPaymentRequest: PlanPaymentRequest) {
        _stripLiveData.value = defaultDataSource.getPaymentData(authToken, planPaymentRequest)
        _stripLiveData.value = null
    }

    suspend fun getPlansDetails(authToken: String, planId: Int) {
        _planDetailsLiveData.value = defaultDataSource.getPlansDetails(authToken, planId)
        _planDetailsLiveData.value = null
    }

    suspend fun updatePaymentStatus(
        authToken: String,
        paymentStatusRequest: UpdatePaymentStatusRequest
    ) {
        _paymentStatusLiveData.value =
            defaultDataSource.updatePaymentStatus(authToken, paymentStatusRequest)
        _paymentStatusLiveData.value = null
    }

    suspend fun getMyPlans(authToken: String, type: Int, page: Int, perPage: Int) {
        if (type == 1) {
            _currentSimLiveData.value = defaultDataSource.getMyPlans(authToken, type, page, perPage)
            _currentSimLiveData.value = null
        } else {
            _activatedSimLiveData.value =
                defaultDataSource.getMyPlans(authToken, type, page, perPage)
            _activatedSimLiveData.value = null
        }
    }
}