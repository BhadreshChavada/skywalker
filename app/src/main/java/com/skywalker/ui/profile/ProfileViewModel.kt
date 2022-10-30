package com.skywalker.ui.profile


import android.icu.util.ULocale.getCountry
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.skywalker.helper.DataStoreManager
import com.skywalker.helper.DataStoreManager.PreferencesKeys.authToken
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
) : ViewModel() {

    val userLiveData = MutableLiveData<UserData>()
    private var authToken = ""



    fun getUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.getUserData().collect {
                it?.let {
                    val gson = Gson()
                    userLiveData.postValue(gson.fromJson(it, UserData::class.java))
                }
            }
        }
    }

}