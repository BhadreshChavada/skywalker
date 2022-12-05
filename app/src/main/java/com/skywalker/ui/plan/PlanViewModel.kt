package com.skywalker.ui.plan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skywalker.baseClass.BaseViewModel
import com.skywalker.helper.DataStoreManager
import com.skywalker.helper.Utils
import com.skywalker.model.request.PlanPaymentRequest
import com.skywalker.model.request.UpdatePaymentStatusRequest
import com.skywalker.model.respone.PlanDataItem
import com.skywalker.model.respone.StripData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanViewModel
@Inject constructor(
    private val planApiRepository: PlanApiRepository,
    private val dataStoreManager: DataStoreManager
) : BaseViewModel(dataStoreManager) {

    val planLiveData = planApiRepository.planLiveData
    val stripLiveData = planApiRepository.stripLiveData
    val planDetailsLiveData = planApiRepository.planDetailsLiveData
    val paymentStatusLiveData = planApiRepository.paymentStatusLiveData

    lateinit var selectedPlanDetails: PlanDataItem
    lateinit var paymentRawDetails: StripData


    fun getPlans(countryInt: Int, type: Int) {
        viewModelScope.launch {
            dataStoreManager.getAuthToken().collect {
                it?.let {
                    getPlans(it, countryInt, type)

                }
            }
        }

    }


    private fun getPlans(authToken: String, countryInt: Int, type: Int) {
        viewModelScope.launch {
            planApiRepository.getPlans(
                authToken, type,
                countryInt, 1, Utils.PAGE_PER_ITEM
            )
        }
    }

    fun getPlanPayment(amount: String, planId: String) {
        var planPaymentRequest = PlanPaymentRequest(amount = amount, planId = planId)
        viewModelScope.launch {
            dataStoreManager.getAuthToken().collect {
                it?.let {
                    planApiRepository.getPaymentData(it, planPaymentRequest)

                }
            }
        }
    }

    fun getPlansDetails(planId: Int) {
        viewModelScope.launch {
            dataStoreManager.getAuthToken().collect {
                it?.let {
                    planApiRepository.getPlansDetails(it, planId)
                }
            }
        }

    }

    fun updatePaymentStatus(paymentStatusRequest: UpdatePaymentStatusRequest) {
        viewModelScope.launch {
            dataStoreManager.getAuthToken().collect {
                it?.let {
                    planApiRepository.updatePaymentStatus(it, paymentStatusRequest)
                }
            }
        }

    }

}