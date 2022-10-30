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
import com.skywalker.databinding.FragmentMoreInfoBinding
import com.skywalker.databinding.FragmentProfileBinding

class MoreInfoFragment : Fragment(R.layout.fragment_more_info) {

    private lateinit var binding: FragmentMoreInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_more_info, container, false
        )
        binding.toolbar.tvTitle.text = getString(R.string.more_info)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
    }

    private fun setClickListener() {
        binding.tvAboutSkywalker.setOnClickListener {
            findNavController().navigate(
                R.id.action_moreInfoFragment_to_aboutSkywalkerFragment
            )
        }

        binding.toolbar.ivBack.setOnClickListener { requireActivity().onBackPressed() }
    }
}