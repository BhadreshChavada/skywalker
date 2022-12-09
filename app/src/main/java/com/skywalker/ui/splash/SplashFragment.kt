package com.skywalker.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.skywalker.BuildConfig
import com.skywalker.R
import com.skywalker.helper.Utils
import com.skywalker.ui.homeTab.MainTabActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashFragment : Fragment() {

    private val splashViewModel: SplashViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retrieveRemoteConfig()
    }

    private fun retrieveRemoteConfig() {
        val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)

        if (!BuildConfig.DEBUG) {
            mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(requireActivity(), object : OnCompleteListener<Boolean?> {
                    override fun onComplete(task: Task<Boolean?>) {
                        if (task.isSuccessful()) {
                            Utils.BASE_URL = mFirebaseRemoteConfig.getString("SIM_APP_BASE_URL")
                            Utils.HOTSPOT_BASE_URL =
                                mFirebaseRemoteConfig.getString("HOTSPOT_APP_BASE_URL")
                            Utils.SUBSCRIPTION_URL =
                                mFirebaseRemoteConfig.getString("SUBSCRIPTION_URL")
                            Utils.TERMS_CONDITION =
                                mFirebaseRemoteConfig.getString("TERMS_CONDITION")
                            Utils.WEB_SITE = mFirebaseRemoteConfig.getString("WEB_SITE")
                            Utils.PRIVACY_POLICY = mFirebaseRemoteConfig.getString("PRIVACY_POLICY")
                            Utils.ABOUT_US = mFirebaseRemoteConfig.getString("ABOUT_US")

                        }
                    }
                })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            splashViewModel.getUserData()
            setObserver()
        }, 3000)
    }

    private fun setObserver() {
        Log.d("SESSION1", "SESSION1")
        splashViewModel.isUserLoggedIn.observe(viewLifecycleOwner) {
            it?.let {
                Log.d("SESSION", it.toString())
                if (it) {
                    val intent = Intent(requireActivity(), MainTabActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    splashViewModel.isWTSeen()
                }
            }
        }

        splashViewModel.isWTSeen.observe(viewLifecycleOwner) {
            it?.let {
                if (it) {
                    findNavController().navigate(R.id.action_splashFragment_to_authenticationFragment)
                } else {

                    findNavController().navigate(R.id.action_splashFragment_to_walkthroughFragment)
                }
            }
        }
    }


}