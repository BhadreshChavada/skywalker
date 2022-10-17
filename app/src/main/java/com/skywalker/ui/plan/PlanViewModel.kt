package com.skywalker.ui.plan

import android.icu.util.ULocale.getCountry
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skywalker.connection.ResultWrapper
import com.skywalker.helper.DataStoreManager
import com.skywalker.helper.SingleLiveEvent
import com.skywalker.helper.ValidationUtils.isValidConfirmPassword
import com.skywalker.helper.ValidationUtils.isValidEmail
import com.skywalker.helper.ValidationUtils.isValidPassword
import com.skywalker.helper.ValidationUtils.isValidUserName
import com.skywalker.model.request.LoginRequest
import com.skywalker.model.request.SignupRequest
import com.skywalker.model.respone.CountryData
import com.skywalker.model.respone.LoginResponse
import com.skywalker.model.respone.SuccessResponse
import com.skywalker.ui.authentication.AuthenticationApiRepository
import com.skywalker.ui.store.StoreApiRepository
import com.skywalker.ui.store.StoreFragment.Companion.type
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
}