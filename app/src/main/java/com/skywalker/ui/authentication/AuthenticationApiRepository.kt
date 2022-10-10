package com.skywalker.ui.authentication

import androidx.lifecycle.MutableLiveData
import com.skywalker.connection.DefaultDataSource
import com.skywalker.connection.ResultWrapper
import com.skywalker.model.request.LoginRequest
import com.skywalker.model.request.SignupRequest
import com.skywalker.model.respone.LoginResponse
import com.skywalker.model.respone.SuccessResponse
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

@Module
@InstallIn(ActivityComponent::class)
class AuthenticationApiRepository @Inject constructor(
    private val defaultDataSource: DefaultDataSource
) {

    private val _signUpLiveData = MutableLiveData<ResultWrapper<SuccessResponse>?>()
    val signUpLiveData: MutableLiveData<ResultWrapper<SuccessResponse>?>
        get() = _signUpLiveData

    private val _loginLiveData = MutableLiveData<ResultWrapper<LoginResponse>?>()
    val loginLiveData: MutableLiveData<ResultWrapper<LoginResponse>?>
        get() = _loginLiveData

    suspend fun doRegisterWithEmail(request: SignupRequest){
        _signUpLiveData.value = defaultDataSource.doRegisterWithEmail(request)
        _signUpLiveData.value = null
    }

    suspend fun doLoginWithEmail(request: LoginRequest){
        _loginLiveData.value = defaultDataSource.doLoginWithEmail(request)
        _loginLiveData.value = null
    }
}