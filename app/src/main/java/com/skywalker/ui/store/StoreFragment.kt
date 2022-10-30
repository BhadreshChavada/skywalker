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
import com.skywalker.helper.PaginationListener
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
    lateinit var globalSimAdapter: GlobalSimAdapter
    private var _isLoading = false
    private var _isLastPage = false

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
        binding.tvGlobalEsim.setOnClickListener { selectedTab(2)}
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
                binding.vpBanner.visibility = VISIBLE
                binding.regionalESim.visibility = GONE
                binding.globalESim.visibility = GONE
            }
            1 -> {
                binding.tvLocalSim.setTextColor(resources.getColor(R.color.black_text))
                binding.tvRegionalSim.setTextColor(resources.getColor(R.color.white))
                binding.tvGlobalEsim.setTextColor(resources.getColor(R.color.black_text))

                binding.tvRegionalSim.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_rectangle_rounded_corner_5_green))
                binding.tvLocalSim.setBackgroundDrawable(null)
                binding.tvGlobalEsim.setBackgroundDrawable(null)

                binding.localESim.visibility = GONE
                binding.vpBanner.visibility = VISIBLE
                binding.regionalESim.visibility = VISIBLE
                binding.globalESim.visibility = GONE
            }
            2 -> {
                binding.tvLocalSim.setTextColor(resources.getColor(R.color.black_text))
                binding.tvRegionalSim.setTextColor(resources.getColor(R.color.black_text))
                binding.tvGlobalEsim.setTextColor(resources.getColor(R.color.white))

                binding.tvGlobalEsim.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_rectangle_rounded_corner_5_green))
                binding.tvLocalSim.setBackgroundDrawable(null)
                binding.tvRegionalSim.setBackgroundDrawable(null)

                binding.vpBanner.visibility = GONE
                binding.localESim.visibility = GONE
                binding.regionalESim.visibility = GONE
                binding.globalESim.visibility = VISIBLE
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mProgressDialog.show()
        setRecycleView()
        setObserver()
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

        val layoutManager = GridLayoutManager(requireActivity(), 4)
        binding.rvCountry.layoutManager = layoutManager
        binding.rvCountry.adapter = countryAdapter

        binding.rvCountry.addOnScrollListener(object : PaginationListener(layoutManager) {
            override fun loadMoreItems() {
                _isLoading = true
                storeViewModel.countryCurrentPage += 1
                storeViewModel.getCountryData()
            }

            override fun isLastPage(): Boolean {
                return _isLastPage;
            }

            override fun isLoading(): Boolean {
                return _isLoading;
            }

        })


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

        globalSimAdapter = GlobalSimAdapter(requireActivity())
        binding.rvGlobal.adapter = globalSimAdapter


    }

    private fun setObserver() {

        storeViewModel.countryLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultWrapper.Success -> if (result.value != null) {
                    // Success code go here
                    mProgressDialog.dismiss()
                    val currentList = countryAdapter.currentList.toMutableList()
                    currentList.addAll(result.value.data!!)
                    countryAdapter.submitList(currentList)
                    popularCountryAdapter.submitList(result.value.data)

                    _isLastPage = result.value.meta.currentPage == storeViewModel.countryCurrentPage


                }
                is ResultWrapper.Error -> {
                    mProgressDialog.dismiss()
                }
                else -> {
                    mProgressDialog.dismiss()
                    // if result value is something else
                }

            }
            storeViewModel.getRegionData()
            storeViewModel.getGlobalESimList()
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

        storeViewModel.planLiveData.observe(viewLifecycleOwner) { result ->

            when (result) {
                is ResultWrapper.Success -> if (result.value != null) {
                    // Success code go here
                    mProgressDialog.dismiss()
                    globalSimAdapter.submitList(result.value.data)
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
            storeViewModel.getCountryData()

        }

    }


}