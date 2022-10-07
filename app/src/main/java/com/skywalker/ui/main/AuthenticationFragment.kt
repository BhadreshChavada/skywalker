package com.skywalker.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.ViewGroupBindingAdapter.setListener
import androidx.fragment.app.Fragment
import com.skywalker.R
import com.skywalker.databinding.FragmentAuthenticationBinding
import com.skywalker.databinding.FragmentWalkthroughBinding
import com.skywalker.ui.main.adapter.WalkthroughAdapter


class AuthenticationFragment : Fragment(R.layout.fragment_authentication) {

    private lateinit var binding: FragmentAuthenticationBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_authentication, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
    }

    private fun setListener() {
        binding.tvSignUp.setOnClickListener {
            binding.tvSignUp.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_rectangle_rounded_corner_green))
            binding.tvLogin.setBackgroundDrawable(null)
            binding.tvSignUp.setTextColor(resources.getColor(R.color.white))
            binding.tvLogin.setTextColor(resources.getColor(R.color.black_text))
            binding.cardSignup.visibility = VISIBLE
            binding.cardSignin.visibility = GONE
        }

        binding.tvLogin.setOnClickListener {
            binding.tvLogin.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_rectangle_rounded_corner_green))
            binding.tvSignUp.setBackgroundDrawable(null)
            binding.tvLogin.setTextColor(resources.getColor(R.color.white))
            binding.tvSignUp.setTextColor(resources.getColor(R.color.black_text))
            binding.cardSignin.visibility = VISIBLE
            binding.cardSignup.visibility = GONE
        }
    }

}