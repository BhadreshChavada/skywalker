package com.skywalker.ui.store


import android.icu.util.ULocale.getCountry
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.skywalker.helper.DataStoreManager
import com.skywalker.helper.DataStoreManager.PreferencesKeys.authToken
import com.skywalker.model.respone.UserData
import com.skywalker.ui.plan.PlanApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel
@Inject constructor(
    private val storeApiRepository: StoreApiRepository,
    private val dataStoreManager: DataStoreManager,
    private val planApiRepository: PlanApiRepository
) : ViewModel() {


    val countryLiveData = storeApiRepository.countryLiveData
    val regionLiveData = storeApiRepository.regionLiveData
    val planLiveData = planApiRepository.planLiveData

    val userLiveData = MutableLiveData<UserData>()
    private var authToken = ""

    var countryCurrentPage = 1
    var regionCurrentPage = 1


    fun getUserData() {
        getAuthToken()
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.getUserData().collect {
                it?.let {
                    val gson = Gson()
                    userLiveData.postValue(gson.fromJson(it, UserData::class.java))
                }
            }
        }
    }

    private fun getAuthToken() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.getAuthToken().collect {
                it?.let {
                    authToken = it

                }
            }
        }

    }

    fun getRegionData() {
        viewModelScope.launch {
            storeApiRepository.getRegions(
                authToken,
                regionCurrentPage,
                25
            )
        }
    }

    fun getCountryData() {
        viewModelScope.launch {
            storeApiRepository.getCountries(
                authToken,
                countryCurrentPage,
                25
            )
        }

    }

    fun getGlobalESimList() {
        viewModelScope.launch {
            planApiRepository.getPlans(
                authToken,
                3,
                0,
                1,
                25
            )
        }

    }
}