package com.skywalker.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.skywalker.R
import com.skywalker.ui.homeTab.MainTabActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashFragment : Fragment() {

    private val splashViewModel: SplashViewModel by activityViewModels()
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