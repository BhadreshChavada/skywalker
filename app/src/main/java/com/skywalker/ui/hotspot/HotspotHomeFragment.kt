package com.skywalker.ui.hotspot

import android.os.Bundle
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
import com.skywalker.databinding.FragmentStoreBinding
import com.skywalker.helper.AdapterItemClickListener
import com.skywalker.helper.ApiProgressDialog
import com.skywalker.helper.Utils
import com.skywalker.model.respone.CountryDataItem
import com.skywalker.model.respone.PlanDataItem
import com.skywalker.model.respone.RegionDataItem
import com.skywalker.ui.plan.PlanViewModel
import com.skywalker.ui.plan.PlansAdapter
import com.skywalker.ui.store.CountryAdapter
import com.skywalker.ui.store.PopularCountryAdapter
import com.skywalker.ui.store.RegionAdapter
import com.skywalker.ui.store.StoreViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HotspotHomeFragment : BaseFragment(R.layout.fragment_store) {

    private lateinit var binding: FragmentStoreBinding

    private val storeViewModel: StoreViewModel by viewModels()
    private val planViewModel: PlanViewModel by activityViewModels()
    private lateinit var mProgressDialog: ApiProgressDialog

    lateinit var countryAdapter: CountryAdapter
    lateinit var regionAdapter: RegionAdapter
    lateinit var popularCountryAdapter: PopularCountryAdapter
    lateinit var globalSimAdapter: PlansAdapter
    private var _isCountryLastPage = false
    private var _isRegionLastPage = false
    private var _isPlanLastPage = false


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
        binding.tvGlobalEsim.setOnClickListener { selectedTab(2) }
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
        setClickListener()
        storeViewModel.countryCurrentPage = 1
        storeViewModel.getUserData()
    }

    private fun setClickListener(){
        binding.tvSkyMoneyAmount.setOnClickListener {
            findNavController().navigate(R.id.action_homeMainFragment_to_walletFragment)
        }
    }

    private fun setRecycleView() {
        popularCountryAdapter = PopularCountryAdapter(requireActivity())
        binding.rvPopularCountry.adapter = popularCountryAdapter

        countryAdapter = CountryAdapter(requireActivity(),
            object : AdapterItemClickListener<CountryDataItem> {
                override fun itemClicked(countryDataItem: CountryDataItem) {
                    val bundle = Bundle()
                    bundle.putString(name, countryDataItem.name)
                    bundle.putInt(HotspotHomeFragment.id, countryDataItem.countryId)
                    bundle.putInt(HotspotHomeFragment.type, 1)
                    findNavController().navigate(
                        R.id.action_homeMainFragment_to_planFragment,
                        bundle
                    )
                }
            })

        val layoutManager = GridLayoutManager(requireActivity(), 4)
        binding.rvCountry.layoutManager = layoutManager
        binding.rvCountry.adapter = countryAdapter


        binding.nsMainView.viewTreeObserver?.addOnScrollChangedListener {
            val view = binding.nsMainView.getChildAt(binding.nsMainView.childCount - 1)
            val diff = view.bottom - (binding.nsMainView.height + binding.nsMainView.scrollY)

            if (diff == 0) {
                if (!_isCountryLastPage && binding.rvCountry.visibility == VISIBLE) {
                    storeViewModel.countryCurrentPage += 1
                    storeViewModel.getCountryData()
                } else if (!_isPlanLastPage && binding.rvGlobal.visibility == VISIBLE) {
                    storeViewModel.planCurrentPage += 1
                    storeViewModel.getGlobalESimList()
                } else if (!_isRegionLastPage && binding.rvRegion.visibility == VISIBLE) {
                    storeViewModel.regionCurrentPage += 1
                    storeViewModel.getRegionData()
                }
            }
        }


        regionAdapter = RegionAdapter(requireActivity(),
            object : AdapterItemClickListener<RegionDataItem> {
                override fun itemClicked(regionDataItem: RegionDataItem) {
                    val bundle = Bundle()
                    bundle.putString(name, regionDataItem.name)
                    bundle.putInt(HotspotHomeFragment.id, regionDataItem.regionId)
                    bundle.putInt(HotspotHomeFragment.type, 2)
                    findNavController().navigate(
                        R.id.action_homeMainFragment_to_planFragment,
                        bundle
                    )
                }
            })
        binding.rvRegion.layoutManager = GridLayoutManager(requireActivity(), 4)
        binding.rvRegion.adapter = regionAdapter

        globalSimAdapter = PlansAdapter(requireActivity(), object : PlansAdapter.PlanAdapterItemClick {
            override fun redirectToDetails(planDataItem: PlanDataItem) {
                val bundle = Bundle()
                bundle.putString("planID", planDataItem.planId.toString())
                findNavController().navigate(
                    R.id.action_homeMainFragment_to_planDetailFragment,
                    bundle
                )
            }

            override fun redirectToPayment(planDataItem: PlanDataItem) {
                mProgressDialog.show()
                planViewModel.selectedPlanDetails = planDataItem
                planViewModel.getPlanPayment(planDataItem.price, planDataItem.planId.toString())

            }

        })
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

                    _isCountryLastPage =
                        result.value.meta.totalPage == storeViewModel.countryCurrentPage

                }
                is ResultWrapper.Error -> {
                    mProgressDialog.dismiss()
                }
                is ResultWrapper.SessionExpired -> {
                    storeViewModel.clearPreference()
                    redirectToLogin()
                }
                else -> {
                    mProgressDialog.dismiss()
                    // if result value is something else
                }

            }
            if (storeViewModel.countryCurrentPage == 1) {
                storeViewModel.getRegionData()
                storeViewModel.getGlobalESimList()
            }
        }

        storeViewModel.regionLiveData.observe(viewLifecycleOwner) { result ->

            when (result) {
                is ResultWrapper.Success -> if (result.value != null) {
                    // Success code go here
                    mProgressDialog.dismiss()
                    regionAdapter.submitList(result.value.data)
                    _isRegionLastPage =
                        result.value.meta.totalPage == storeViewModel.regionCurrentPage
                }
                is ResultWrapper.Error -> {
                    mProgressDialog.dismiss()
                }
                is ResultWrapper.SessionExpired -> {
                    storeViewModel.clearPreference()
                    redirectToLogin()
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
                    _isPlanLastPage = result.value.meta.totalPage == storeViewModel.planCurrentPage
                }
                is ResultWrapper.Error -> {
                    mProgressDialog.dismiss()
                }
                is ResultWrapper.SessionExpired -> {
                    storeViewModel.clearPreference()
                    redirectToLogin()
                }
                else -> {
                    mProgressDialog.dismiss()
                    // if result value is something else
                }
            }
        }

        storeViewModel.userLiveData.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvTitle.text = it.userName
                storeViewModel.getCountryData()
            }

        }

        planViewModel.stripLiveData.observe(viewLifecycleOwner) { result ->
            mProgressDialog.dismiss()
            when (result) {
                is ResultWrapper.Success -> if (result.value != null) {
                    // Success code go here
                    planViewModel.paymentRawDetails = result.value
                    findNavController().navigate(
                        R.id.action_mainFragment_to_paymentConfirmationFragment
                    )
                }
                is ResultWrapper.Error -> {
                    Utils.showSnackBar(
                        binding.root,
                        result.errorResponse?.message!!,
                        true,
                        requireActivity()
                    )
                }
                else -> {

                    // if result value is something else
                }
            }

        }

    }

}