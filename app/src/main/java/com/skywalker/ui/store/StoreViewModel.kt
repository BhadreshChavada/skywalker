package com.skywalker.ui.store

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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel
@Inject constructor(
    private val storeApiRepository: StoreApiRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {


    val countryLiveData = storeApiRepository.countryLiveData


    init {

        viewModelScope.launch {
            dataStoreManager.getAuthToken().collect {
                it?.let {
                    getCountryRegion(it)

                }
            }
        }

    }

    private fun getCountryRegion(authToken: String) {
        viewModelScope.launch {
            storeApiRepository.getCountries(
                authToken,
                1,
                25
            )

            storeApiRepository.getRegions(
                authToken,
                1,
                25
            )
        }
    }
}