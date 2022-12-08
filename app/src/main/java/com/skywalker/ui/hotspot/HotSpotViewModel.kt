package com.skywalker.ui.hotspot


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.skywalker.baseClass.BaseViewModel
import com.skywalker.connection.ResultWrapper
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

    private val _hotSpotData = MutableLiveData<ResultWrapper<List<HotspotDetails>>?>()
    val hotSpotData: MutableLiveData<ResultWrapper<List<HotspotDetails>>?>
        get() = _hotSpotData

    lateinit var selectedHotspotList : HotspotDetails

    init {
        getHotspotData()
    }

    private fun getHotspotData() {
        viewModelScope.launch(Dispatchers.Main) {
            _hotSpotData.value = hotspotApiRepository.getHotspotData()
        }
    }


}