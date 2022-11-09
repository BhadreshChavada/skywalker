package com.skywalker.ui.mySim

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.skywalker.R
import com.skywalker.connection.ResultWrapper
import com.skywalker.databinding.FragmentMySimBinding
import com.skywalker.helper.ApiProgressDialog
import com.skywalker.model.respone.PlanDataItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyESimFragment : Fragment(R.layout.fragment_my_sim) {

    private lateinit var binding: FragmentMySimBinding

    private val myESimViewModel: MyESimViewModel by viewModels()
    private lateinit var mProgressDialog: ApiProgressDialog

    lateinit var activatedSimAdapter: ActivatedSimAdapter
    lateinit var currentSimAdapter: CurrentSimAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_my_sim, container, false
        )
        mProgressDialog = ApiProgressDialog(requireActivity())
        setTabListener()
        return binding.root
    }

    private fun setTabListener() {
        binding.tvCurrentSim.setOnClickListener { selectedTab(0) }
        binding.tvActivatedSim.setOnClickListener { selectedTab(1) }
    }

    private fun selectedTab(position: Int) {
        when (position) {
            0 -> {
                binding.tvCurrentSim.setTextColor(resources.getColor(R.color.white))
                binding.tvActivatedSim.setTextColor(resources.getColor(R.color.black_text))

                binding.tvCurrentSim.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_rectangle_rounded_corner_5_green))
                binding.tvActivatedSim.setBackgroundDrawable(null)

                binding.rvCurrentSim.visibility = View.VISIBLE
                binding.rvActivatedSim.visibility = View.GONE
            }
            1 -> {
                binding.tvCurrentSim.setTextColor(resources.getColor(R.color.black_text))
                binding.tvActivatedSim.setTextColor(resources.getColor(R.color.white))

                binding.tvActivatedSim.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_rectangle_rounded_corner_5_green))
                binding.tvCurrentSim.setBackgroundDrawable(null)

                binding.rvCurrentSim.visibility = View.GONE
                binding.rvActivatedSim.visibility = View.VISIBLE
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mProgressDialog.show()
        setRecycleView()
        setObserver()
        myESimViewModel.currentSimCurrentPage = 1
        myESimViewModel.getMyPlans(1)
    }

    private fun setRecycleView() {
        activatedSimAdapter = ActivatedSimAdapter(requireActivity(),
            object : ActivatedSimAdapter.PlanAdapterItemClick {
                override fun redirectToDetails(planDataItem: PlanDataItem) {

                }

            })
        binding.rvActivatedSim.adapter = activatedSimAdapter

        currentSimAdapter = CurrentSimAdapter(requireActivity(),
            object : CurrentSimAdapter.PlanAdapterItemClick {
                override fun redirectToDetails(planDataItem: PlanDataItem) {

                }

            })
        binding.rvCurrentSim.adapter = activatedSimAdapter

    }

    private fun setObserver() {

        myESimViewModel.currentSimLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultWrapper.Success -> if (result.value != null) {
                    // Success code go here
                    mProgressDialog.dismiss()
                    currentSimAdapter.submitList(result.value.data)
                }
                is ResultWrapper.Error -> {
                    mProgressDialog.dismiss()
                }
                else -> {
                    mProgressDialog.dismiss()
                }
            }
            if (myESimViewModel.currentSimCurrentPage == 1) {
                myESimViewModel.getMyPlans(2)
            }
        }
        myESimViewModel.activatedSimLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultWrapper.Success -> if (result.value != null) {
                    // Success code go here
                    activatedSimAdapter.submitList(result.value.data)
                }
                is ResultWrapper.Error -> {
                    mProgressDialog.dismiss()
                }
                else -> {
                    mProgressDialog.dismiss()
                }
            }
        }


    }
}