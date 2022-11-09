package com.skywalker.ui.profile

import androidx.lifecycle.MutableLiveData
import com.skywalker.connection.DefaultDataSource
import com.skywalker.connection.ResultWrapper
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


    suspend fun getUserDetails(authToken: String) {
        _userLiveData.value = defaultDataSource.getUserData(authToken)
        _userLiveData.value = null
    }

    suspend fun updateUserDetails(authToken: String,updateProfileRequest: UpdateProfileRequest) {
        _userLiveData.value = defaultDataSource.updateUserDetails(authToken,updateProfileRequest)
        _userLiveData.value = null
    }


}