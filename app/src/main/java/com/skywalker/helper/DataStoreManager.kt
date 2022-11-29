package com.skywalker.helper

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.skywalker.helper.DataStoreManager.PreferencesKeys.authToken
import com.skywalker.helper.DataStoreManager.PreferencesKeys.freshInstalled
import com.skywalker.helper.DataStoreManager.PreferencesKeys.wtStatus
import com.skywalker.model.respone.UserData
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(private val dataStore: DataStore<Preferences>) {

    object PreferencesKeys {
        val authToken = stringPreferencesKey("auth_token")
        val userData = stringPreferencesKey("user_data")
        val wtStatus = stringPreferencesKey("wt_status")
        val freshInstalled = stringPreferencesKey("fresh_installed")
    }

    suspend fun storeAuthToken(authToken: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.authToken] = authToken ?: ""
        }
    }

    fun getAuthToken() = dataStore.data.map {
        it[authToken]
    }

    suspend fun isWTSeen(seen: Boolean) {
        dataStore.edit { preferences ->
            preferences[wtStatus] = (seen).toString()
        }
    }

    fun getWTSeen() = dataStore.data.map {
        it[wtStatus]
    }

    suspend fun isFreshInstalled(status: Boolean) {
        dataStore.edit { preferences ->
            preferences[freshInstalled] = (status).toString()
        }
    }

    fun getFreshInstalled() = dataStore.data.map {
        it[freshInstalled]
    }

    suspend fun storeUserData(userData: UserData?) {
        if (userData != null) {
            val gson = Gson()
            val strUser = gson.toJson(userData)
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.userData] = strUser ?: ""
            }
        } else {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.userData] = ""
            }
        }
    }

    fun getUserData() = dataStore.data.map {
        it[PreferencesKeys.userData]
    }

    suspend fun clearPrefs() {
        dataStore.edit { preference -> preference.clear() }
    }

    suspend fun clearUserDataPrefs() {
        dataStore.edit {
            it.clear()
        }
    }

}