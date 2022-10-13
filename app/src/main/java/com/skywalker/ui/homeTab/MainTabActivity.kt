package com.skywalker.ui.homeTab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.skywalker.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainTabActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_main)
    }
}