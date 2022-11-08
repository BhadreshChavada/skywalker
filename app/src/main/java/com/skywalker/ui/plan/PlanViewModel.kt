package com.skywalker.ui.plan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skywalker.helper.DataStoreManager
import com.skywalker.model.request.PlanPaymentRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanViewModel
@Inject constructor(
    private val planApiRepository: PlanApiRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    val planLiveData = planApiRepository.planLiveData
    val stripLiveData = planApiRepository.stripLiveData
    val planDetailsLiveData = planApiRepository.planDetailsLiveData


    fun getPlans(countryInt: Int,type: Int) {

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
                countryInt, 1, 25
            )
        }
    }

    fun getPlanPayment(amount:String,planId:String){

        var planPaymentRequest = PlanPaymentRequest(amount = amount,planId =planId)
        viewModelScope.launch {
            dataStoreManager.getAuthToken().collect {
                it?.let {
                    planApiRepository.getPaymentData(it,planPaymentRequest)

                }
            }


        }
    }

    fun getPlansDetails(planId: Int) {

        viewModelScope.launch {
            dataStoreManager.getAuthToken().collect {
                it?.let {
                    planApiRepository.getPlansDetails(it,planId)

                }
            }
        }

    }

    fun clearPreference() {
        viewModelScope.launch {
            dataStoreManager.clearPrefs()
        }
    }
}