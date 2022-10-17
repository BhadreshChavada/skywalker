package com.skywalker.ui.store

import androidx.lifecycle.MutableLiveData
import com.skywalker.connection.DefaultDataSource
import com.skywalker.connection.ResultWrapper
import com.skywalker.model.respone.CountryData
import com.skywalker.model.respone.RegionResponse
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

@Module
@InstallIn(ActivityComponent::class)
class StoreApiRepository @Inject constructor(
    private val defaultDataSource: DefaultDataSource,
) {

    private val _countryLiveData = MutableLiveData<ResultWrapper<CountryData>?>()
    val countryLiveData: MutableLiveData<ResultWrapper<CountryData>?>
        get() = _countryLiveData

    private val _regionLiveData = MutableLiveData<ResultWrapper<RegionResponse>?>()
    val regionLiveData: MutableLiveData<ResultWrapper<RegionResponse>?>
        get() = _regionLiveData

    suspend fun getCountries(authToken: String, page: Int, perPage: Int) {
        _countryLiveData.value = defaultDataSource.getCountries(authToken, page, perPage)
        _countryLiveData.value = null
    }

    suspend fun getRegions(authToken: String, page: Int, perPage: Int) {
        _regionLiveData.value = defaultDataSource.getRegions(authToken, page, perPage)
        _regionLiveData.value = null
    }
}