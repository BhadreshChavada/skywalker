package com.skywalker

import android.app.Application
import com.skywalker.connection.ConnectivityStatusProvider
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class Skywalker : Application() {

    @Inject
    lateinit var connectivityStatusProvider: ConnectivityStatusProvider

    override fun onCreate() {
        super.onCreate()
        connectivityStatusProvider.setConsumerApp(this)
    }
}