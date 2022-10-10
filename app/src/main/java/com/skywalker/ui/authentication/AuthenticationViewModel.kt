package com.skywalker.ui.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skywalker.connection.ResultWrapper
import com.skywalker.helper.SingleLiveEvent
import com.skywalker.helper.ValidationUtils.isValidConfirmPassword
import com.skywalker.helper.ValidationUtils.isValidEmail
import com.skywalker.helper.ValidationUtils.isValidPassword
import com.skywalker.helper.ValidationUtils.isValidUserName
import com.skywalker.model.request.LoginRequest
import com.skywalker.model.request.SignupRequest
import com.skywalker.model.respone.LoginResponse
import com.skywalker.model.respone.SuccessResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel
@Inject constructor(
    private val loginApiRepository: AuthenticationApiRepository
) : ViewModel() {

    var loginRequest = LoginRequest()
    var signupRequest = SignupRequest()
    val mLoginData: MutableLiveData<ResultWrapper<LoginResponse>?> =
        loginApiRepository.loginLiveData

    val mSignUpData: MutableLiveData<ResultWrapper<SuccessResponse>?> =
        loginApiRepository.signUpLiveData

    val showErrorLiveData = SingleLiveEvent<String>()

    fun doSignUp() {
        viewModelScope.launch {
            if (!isValidUserName(signupRequest.userName)) {
                showErrorLiveData.value = "Enter valid username"
            } else if (!signupRequest.email.isValidEmail()) {
                showErrorLiveData.value = "Enter valid email"
            } else if (!isValidPassword(signupRequest.password)) {
                showErrorLiveData.value =
                    "Your password must be at least 8 characters long, contain at least one number and have a mixture of uppercase and lowercase letters."
            } else if (!isValidConfirmPassword(
                    signupRequest.password,
                    signupRequest.confirmPassword
                )
            ) {
                showErrorLiveData.value = "Password and confirm password isn't match"
            } else {
                loginApiRepository.doRegisterWithEmail(signupRequest)
            }

        }
    }

    fun doLogin() {
        viewModelScope.launch {
            if (!loginRequest.email.isValidEmail()) {
                showErrorLiveData.value = "Enter valid email"
            } else if (loginRequest.password.isEmpty()) {
                showErrorLiveData.value = "Enter valid password"
            } else {
                loginApiRepository.doLoginWithEmail(loginRequest)
            }
        }
    }

}