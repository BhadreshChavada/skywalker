package com.skywalker.helper

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.skywalker.model.respone.UserData
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(private val dataStore: DataStore<Preferences>) {

    object PreferencesKeys {
        val authToken = stringPreferencesKey("auth_token")
        val userData = stringPreferencesKey("user_data")
    }

    suspend fun storeAuthToken(authToken: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.authToken] = authToken ?: ""
        }
    }

    fun getAuthToken() = dataStore.data.map {
        it[PreferencesKeys.authToken]
    }

    suspend fun storeUserData(userData: UserData) {
        val gson = Gson()
        val strUser = gson.toJson(userData)
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.userData] = strUser ?: ""
        }
    }

    fun getUserData() = dataStore.data.map {
        it[PreferencesKeys.userData]
    }

    suspend fun clearPrefs(){
        dataStore.edit { preference -> preference.clear() }
    }

}