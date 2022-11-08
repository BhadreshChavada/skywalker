package com.skywalker.ui.plan

import androidx.lifecycle.MutableLiveData
import com.skywalker.connection.DefaultDataSource
import com.skywalker.connection.ResultWrapper
import com.skywalker.model.request.LoginRequest
import com.skywalker.model.request.PlanPaymentRequest
import com.skywalker.model.request.SignupRequest
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


    suspend fun getPlans(authToken: String, type: Int, countryId: Int, page: Int, perPage: Int) {
        _planLiveData.value = defaultDataSource.getPlans(authToken, type, countryId, page, perPage)
        _planLiveData.value = null
    }

    suspend fun getPaymentData(authToken: String, planPaymentRequest: PlanPaymentRequest) {
        _stripLiveData.value = defaultDataSource.getPaymentData(authToken, planPaymentRequest)
        _stripLiveData.value = null
    }

    suspend fun getPlansDetails(authToken: String, planId:Int) {
        _planDetailsLiveData.value = defaultDataSource.getPlansDetails(authToken, planId)
        _planDetailsLiveData.value = null
    }
}