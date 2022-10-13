package com.skywalker.ui.splash

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skywalker.helper.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
@Inject constructor(
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {

    private val _isUserLoggedIn = MutableLiveData<Boolean>()
    val isUserLoggedIn: MutableLiveData<Boolean>
        get() = _isUserLoggedIn


    fun getUserData() {
        viewModelScope.launch {
            dataStoreManager.getAuthToken().collect {
                if(it == null){
                    isUserLoggedIn.value = false
                }else{
                    isUserLoggedIn.value = it.isNotEmpty()
                }
            }
        }
    }
}