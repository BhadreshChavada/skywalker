package com.skywalker.ui.store


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.skywalker.helper.DataStoreManager
import com.skywalker.model.respone.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel
@Inject constructor(
    private val storeApiRepository: StoreApiRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {


    val countryLiveData = storeApiRepository.countryLiveData
    val regionLiveData = storeApiRepository.regionLiveData
    val userLiveData  = MutableLiveData<UserData>()


    fun getCountryData() {

        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.getAuthToken().collect {
                it?.let {
                    getCountryRegion(it)

                }
            }


        }


    }

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