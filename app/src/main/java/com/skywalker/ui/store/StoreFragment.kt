package com.skywalker.ui.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.skywalker.R
import com.skywalker.connection.ResultWrapper
import com.skywalker.databinding.FragmentStoreBinding
import com.skywalker.helper.AdapterItemClickListener
import com.skywalker.helper.ApiProgressDialog
import com.skywalker.model.respone.CountryDataItem
import com.skywalker.model.respone.RegionDataItem
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StoreFragment : Fragment(R.layout.fragment_store) {

    private lateinit var binding: FragmentStoreBinding

    private val storeViewModel: StoreViewModel by viewModels()
    private lateinit var mProgressDialog: ApiProgressDialog

    lateinit var countryAdapter: CountryAdapter
    lateinit var regionAdapter: RegionAdapter
    lateinit var popularCountryAdapter: PopularCountryAdapter

    companion object {
        const val id = "id"
        const val name = "name"
        const val type = "type"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_store, container, false
        )
        mProgressDialog = ApiProgressDialog(requireActivity())
        setTabListener()
        return binding.root
    }

    private fun setTabListener() {
        binding.tvLocalSim.setOnClickListener { selectedTab(0) }
        binding.tvRegionalSim.setOnClickListener { selectedTab(1) }
        binding.tvGlobalEsim.setOnClickListener { }
    }

    fun selectedTab(position: Int) {
        when (position) {
            0 -> {
                binding.tvLocalSim.setTextColor(resources.getColor(R.color.white))
                binding.tvRegionalSim.setTextColor(resources.getColor(R.color.black_text))
                binding.tvGlobalEsim.setTextColor(resources.getColor(R.color.black_text))

                binding.tvLocalSim.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_rectangle_rounded_corner_5_green))
                binding.tvRegionalSim.setBackgroundDrawable(null)
                binding.tvGlobalEsim.setBackgroundDrawable(null)

                binding.localESim.visibility = VISIBLE
                binding.regionalESim.visibility = GONE
            }
            1 -> {
                binding.tvLocalSim.setTextColor(resources.getColor(R.color.black_text))
                binding.tvRegionalSim.setTextColor(resources.getColor(R.color.white))
                binding.tvGlobalEsim.setTextColor(resources.getColor(R.color.black_text))

                binding.tvRegionalSim.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_rectangle_rounded_corner_5_green))
                binding.tvLocalSim.setBackgroundDrawable(null)
                binding.tvGlobalEsim.setBackgroundDrawable(null)

                binding.localESim.visibility = GONE
                binding.regionalESim.visibility = VISIBLE
            }
            2 -> {
                binding.tvLocalSim.setTextColor(resources.getColor(R.color.black_text))
                binding.tvRegionalSim.setTextColor(resources.getColor(R.color.black_text))
                binding.tvGlobalEsim.setTextColor(resources.getColor(R.color.white))

                binding.tvGlobalEsim.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_rectangle_rounded_corner_5_green))
                binding.tvLocalSim.setBackgroundDrawable(null)
                binding.tvRegionalSim.setBackgroundDrawable(null)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mProgressDialog.show()
        setRecycleView()
        setObserver()
        storeViewModel.getCountryData()
        storeViewModel.getUserData()
    }

    private fun setRecycleView() {
        popularCountryAdapter = PopularCountryAdapter(requireActivity())
        binding.rvPopularCountry.adapter = popularCountryAdapter

        countryAdapter = CountryAdapter(requireActivity(),
            object : AdapterItemClickListener<CountryDataItem> {
                override fun itemClicked(countryDataItem: CountryDataItem) {
                    val bundle = Bundle()
                    bundle.putString(name, countryDataItem.name)
                    bundle.putInt(StoreFragment.id, countryDataItem.countryId)
                    bundle.putInt(StoreFragment.type, 1)
                    findNavController().navigate(
                        R.id.action_homeMainFragment_to_planFragment,
                        bundle
                    )
                }
            })
        binding.rvCountry.layoutManager = GridLayoutManager(requireActivity(), 4)
        binding.rvCountry.adapter = countryAdapter


        regionAdapter = RegionAdapter(requireActivity(),
            object : AdapterItemClickListener<RegionDataItem> {
                override fun itemClicked(regionDataItem: RegionDataItem) {
                    val bundle = Bundle()
                    bundle.putString(name, regionDataItem.name)
                    bundle.putInt(StoreFragment.id, regionDataItem.regionId)
                    bundle.putInt(StoreFragment.type, 2)
                    findNavController().navigate(
                        R.id.action_homeMainFragment_to_planFragment,
                        bundle
                    )
                }
            })
        binding.rvRegion.layoutManager = GridLayoutManager(requireActivity(), 4)
        binding.rvRegion.adapter = regionAdapter


    }

    private fun setObserver() {

        storeViewModel.countryLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultWrapper.Success -> if (result.value != null) {
                    // Success code go here
                    mProgressDialog.dismiss()

                    countryAdapter.submitList(result.value.data)
                    popularCountryAdapter.submitList(result.value.data)

                }
                is ResultWrapper.Error -> {
                    mProgressDialog.dismiss()
                }
                else -> {
                    mProgressDialog.dismiss()
                    // if result value is something else
                }
            }
        }

        storeViewModel.regionLiveData.observe(viewLifecycleOwner) { result ->

            when (result) {
                is ResultWrapper.Success -> if (result.value != null) {
                    // Success code go here
                    mProgressDialog.dismiss()
                    regionAdapter.submitList(result.value.data)
                }
                is ResultWrapper.Error -> {
                    mProgressDialog.dismiss()
                }
                else -> {
                    mProgressDialog.dismiss()
                    // if result value is something else
                }
            }
        }

        storeViewModel.userLiveData.observe(viewLifecycleOwner) {
            binding.tvTitle.text = it.userName
        }

    }


}