package com.skywalker.ui.hotspot

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.ViewBindingAdapter.setClickListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.skywalker.R
import com.skywalker.baseClass.BaseFragment
import com.skywalker.connection.ResultWrapper
import com.skywalker.databinding.FragmentHotspotHomeBinding
import com.skywalker.databinding.FragmentStoreBinding
import com.skywalker.helper.AdapterItemClickListener
import com.skywalker.helper.ApiProgressDialog
import com.skywalker.helper.Utils
import com.skywalker.model.respone.CountryDataItem
import com.skywalker.model.respone.HotspotDetails
import com.skywalker.model.respone.PlanDataItem
import com.skywalker.model.respone.RegionDataItem
import com.skywalker.ui.plan.PlanViewModel
import com.skywalker.ui.plan.PlansAdapter
import com.skywalker.ui.store.CountryAdapter
import com.skywalker.ui.store.PopularCountryAdapter
import com.skywalker.ui.store.RegionAdapter
import com.skywalker.ui.store.StoreFragment.Companion.id
import com.skywalker.ui.store.StoreFragment.Companion.name
import com.skywalker.ui.store.StoreViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HotspotHomeFragment : BaseFragment(R.layout.fragment_hotspot_home) {

    private lateinit var binding: FragmentHotspotHomeBinding

    private val mViewModel: HotSpotViewModel by activityViewModels()
    private lateinit var mProgressDialog: ApiProgressDialog
    private lateinit var hotspotAdapter: HotspotAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mProgressDialog = ApiProgressDialog(requireActivity())
        mProgressDialog.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_hotspot_home, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListener()
        setObserver()
        setRecycleView()
    }

    private fun setRecycleView() {
        val layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rvHotspot.layoutManager = layoutManager
        hotspotAdapter =
            HotspotAdapter(requireActivity(), object : HotspotAdapter.HotSpotItemClick {
                override fun redirectToDetails(hotspotDetails: HotspotDetails) {
                    mViewModel.selectedHotspotList = hotspotDetails
                    findNavController().navigate(R.id.action_mainFragment_to_fragmentHotspotDetails)
                }

            })
        binding.rvHotspot.adapter = hotspotAdapter
    }

    private fun setClickListener() {
        binding.vpBanner.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_cardActivationFirstStepFragment)
        }
        binding.btnActivateHotspot.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_cardActivationFirstStepFragment)
        }
    }

    private fun setObserver() {
        mViewModel.hotSpotData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultWrapper.Success -> if (result.value != null) {
                    hotspotAdapter.submitList(result.value)
                    mProgressDialog.dismiss()
                }
                is ResultWrapper.Error -> {
                    mProgressDialog.dismiss()
                }
                is ResultWrapper.SessionExpired -> {
                    redirectToLogin()
                }
                else -> {
                    mProgressDialog.dismiss()
                }
            }
        }
    }

}