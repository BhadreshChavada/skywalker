package com.skywalker.ui.plan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.skywalker.R
import com.skywalker.databinding.FragmentSimAdditionalInfoBinding
import com.skywalker.model.respone.PlanDataItem
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PlanAdditionalInfoFragment : Fragment(R.layout.fragment_sim_additional_info) {

    private lateinit var binding: FragmentSimAdditionalInfoBinding
    private val planViewModel: PlanViewModel by activityViewModels()

    @RequiresApi(33)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sim_additional_info, container, false
        )
        setListener()
        val planDetails = planViewModel.selectedPlanDetails
        binding.toolbar.tvTitle.text = getString(R.string.additional_information)
        binding.data = planDetails
        return binding.root
    }

    private fun setListener() {
        binding.toolbar.ivBack.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }

    }

}