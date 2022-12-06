package com.skywalker.ui.profile

import androidx.lifecycle.MutableLiveData
import com.skywalker.connection.DefaultDataSource
import com.skywalker.connection.ResultWrapper
import com.skywalker.helper.DataStoreManager.PreferencesKeys.authToken
import com.skywalker.model.request.UpdateProfileRequest
import com.skywalker.model.respone.*
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

@Module
@InstallIn(ActivityComponent::class)
class SettingApiRepository @Inject constructor(
    private val defaultDataSource: DefaultDataSource,
) {

    private val _userLiveData = MutableLiveData<ResultWrapper<LoginResponse>?>()
    val userLiveData: MutableLiveData<ResultWrapper<LoginResponse>?>
        get() = _userLiveData

    private val _orderHistoryLiveData = MutableLiveData<ResultWrapper<PlanResponse>?>()
    val orderHistoryLiveData: MutableLiveData<ResultWrapper<PlanResponse>?>
        get() = _orderHistoryLiveData


    suspend fun getUserDetails(authToken: String) {
        _userLiveData.value = defaultDataSource.getUserData(authToken)
        _userLiveData.value = null
    }

    suspend fun updateUserDetails(authToken: String, updateProfileRequest: UpdateProfileRequest) {
        _userLiveData.value = defaultDataSource.updateUserDetails(authToken, updateProfileRequest)
        _userLiveData.value = null
    }

    suspend fun getOrderHistory(authToken: String, page: Int, perPage: Int) {
        _orderHistoryLiveData.value = defaultDataSource.getOrderHistory(authToken, page, perPage)
        _orderHistoryLiveData.value = null
    }
}