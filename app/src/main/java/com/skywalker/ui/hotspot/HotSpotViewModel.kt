package com.skywalker.ui.hotspot


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.skywalker.baseClass.BaseViewModel
import com.skywalker.helper.DataStoreManager
import com.skywalker.helper.DataStoreManager.PreferencesKeys.authToken
import com.skywalker.helper.Utils
import com.skywalker.model.respone.HotspotDetails
import com.skywalker.model.respone.UserData
import com.skywalker.ui.plan.PlanApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HotSpotViewModel
@Inject constructor(
    private val hotspotApiRepository: HotspotApiRepository,
    private val dataStoreManager: DataStoreManager
) : BaseViewModel(dataStoreManager) {

    val hotspotList = hotspotApiRepository.hotSpotData
    lateinit var selectedHotspotList : HotspotDetails

    fun getHotspotData() {
        viewModelScope.launch {
            hotspotApiRepository.getHotspotData()
        }
    }


}