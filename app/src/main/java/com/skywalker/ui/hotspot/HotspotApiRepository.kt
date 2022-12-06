package com.skywalker.ui.hotspot

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
class HotspotApiRepository @Inject constructor(
    private val defaultDataSource: DefaultDataSource,
) {

    private val _hotSpotData = MutableLiveData<ResultWrapper<List<HotspotDetails>>?>()
    val hotSpotData: MutableLiveData<ResultWrapper<List<HotspotDetails>>?>
        get() = _hotSpotData

    suspend fun getHotspotData() {
        _hotSpotData.value = defaultDataSource.getHotSpotData()
        _hotSpotData.value = null
    }

}