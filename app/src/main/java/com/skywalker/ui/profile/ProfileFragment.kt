package com.skywalker.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.ViewBindingAdapter.setClickListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.skywalker.R
import com.skywalker.databinding.FragmentProfileBinding
import com.skywalker.databinding.FragmentStoreBinding
import com.skywalker.helper.ApiProgressDialog

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
    }

    private fun setClickListener() {
        binding.tvAccountInformation.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeMainFragment_to_accountInfoFragment
            )
        }

        binding.tvOrders.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeMainFragment_to_orderHistoryFragment
            )
        }

        binding.tvHelpCenter.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeMainFragment_to_moreInfoFragment
            )
        }

    }
}