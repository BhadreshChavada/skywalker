package com.skywalker

import android.app.Application
import com.skywalker.connection.ConnectivityStatusProvider
import com.skywalker.helper.Utils.STRIPE_PUBLIC_KEY
import com.stripe.android.PaymentConfiguration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class Skywalker : Application() {

    @Inject
    lateinit var connectivityStatusProvider: ConnectivityStatusProvider


    override fun onCreate() {
        super.onCreate()
        connectivityStatusProvider.setConsumerApp(this)

        PaymentConfiguration.init(
            applicationContext,
            STRIPE_PUBLIC_KEY
        )
    }
}