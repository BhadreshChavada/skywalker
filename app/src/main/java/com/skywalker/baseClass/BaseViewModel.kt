package com.skywalker.baseClass

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skywalker.helper.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
open class BaseViewModel
@Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    fun clearPreference() {
        viewModelScope.launch {
            dataStoreManager.clearUserDataPrefs()
        }
    }
}