package com.skywalker.ui.profile


import android.icu.util.ULocale.getCountry
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.skywalker.baseClass.BaseViewModel
import com.skywalker.helper.DataStoreManager
import com.skywalker.helper.DataStoreManager.PreferencesKeys.authToken
import com.skywalker.model.request.UpdateProfileRequest
import com.skywalker.model.respone.UserData
import com.skywalker.ui.plan.PlanApiRepository
import com.skywalker.ui.store.StoreApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val settingApiRepository: SettingApiRepository,
) : BaseViewModel(dataStoreManager) {

    val userLiveData = settingApiRepository.userLiveData
    val orderHistoryLiveData = settingApiRepository.orderHistoryLiveData
    private var authToken = ""


    fun getUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.getAuthToken().collect {
                it?.let {
                    authToken = it
                    getUser()
                }
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            settingApiRepository.getUserDetails(
                authToken
            )
        }
    }

    fun updateProfile(updateProfileRequest: UpdateProfileRequest) {
        viewModelScope.launch {
            settingApiRepository.updateUserDetails(
                authToken, updateProfileRequest
            )
        }
    }

    fun getOrderHistory() {
        viewModelScope.launch {
            dataStoreManager.getAuthToken().collect {
                it?.let {
                    authToken = it
                    settingApiRepository.getOrderHistory(
                        authToken, 1, 25
                    )
                }
            }

        }
    }

}