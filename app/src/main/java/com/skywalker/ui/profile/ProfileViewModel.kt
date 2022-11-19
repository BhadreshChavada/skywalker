package com.skywalker.ui.profile


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.skywalker.baseClass.BaseViewModel
import com.skywalker.helper.DataStoreManager
import com.skywalker.model.request.UpdateProfileRequest
import com.skywalker.model.respone.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.gson.Gson

@HiltViewModel
class ProfileViewModel
@Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val settingApiRepository: SettingApiRepository,
) : BaseViewModel(dataStoreManager) {

    val userLiveData = settingApiRepository.userLiveData
    val orderHistoryLiveData = settingApiRepository.orderHistoryLiveData
    private var authToken = ""
    val userSPLiveData = MutableLiveData<UserData>()


    fun getUserReferralCode() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.getUserData().collect {
                it?.let {
                    val gson = Gson()
                    userSPLiveData.postValue(gson.fromJson(it, UserData::class.java))
                }
            }
        }
    }

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