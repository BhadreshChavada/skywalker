package com.skywalker.ui.mySim

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skywalker.baseClass.BaseViewModel
import com.skywalker.helper.DataStoreManager
import com.skywalker.model.request.PlanPaymentRequest
import com.skywalker.model.request.UpdatePaymentStatusRequest
import com.skywalker.model.respone.PlanDataItem
import com.skywalker.ui.plan.PlanApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyESimViewModel
@Inject constructor(
    private val planApiRepository: PlanApiRepository,
    private val dataStoreManager: DataStoreManager
) : BaseViewModel(dataStoreManager) {

    val currentSimLiveData = planApiRepository.currentSimLiveData
    val activatedSimLiveData = planApiRepository.activatedSimLiveData

    lateinit var selectedPlanDetails: PlanDataItem

    var currentSimCurrentPage = 1
    var activatedSimCurrentPage = 1

    fun getMyPlans(type: Int) {
        viewModelScope.launch {
            dataStoreManager.getAuthToken().collect {
                it?.let {
                    getPlans(it, type)

                }
            }
        }

    }


    private fun getPlans(authToken: String, type: Int) {
        viewModelScope.launch {
            if(type==1){
                planApiRepository.getMyPlans(
                    authToken, type,
                    currentSimCurrentPage, 25
                )
            }else{
                planApiRepository.getMyPlans(
                    authToken, type,
                    activatedSimCurrentPage, 25
                )
            }

        }
    }

}