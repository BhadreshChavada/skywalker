package com.skywalker.ui.plan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.skywalker.R
import com.skywalker.baseClass.BaseFragment
import com.skywalker.connection.ResultWrapper
import com.skywalker.databinding.FragmentCountryWiseSimProviderBinding
import com.skywalker.helper.ApiProgressDialog
import com.skywalker.helper.Utils
import com.skywalker.model.respone.PlanDataItem
import com.skywalker.ui.store.StoreFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PlanFragment : BaseFragment(R.layout.fragment_country_wise_sim_provider) {

    private lateinit var binding: FragmentCountryWiseSimProviderBinding

    private val planViewModel: PlanViewModel by activityViewModels()
    private lateinit var mProgressDialog: ApiProgressDialog

    lateinit var planAdapter: PlansAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_country_wise_sim_provider, container, false
        )
        mProgressDialog = ApiProgressDialog(requireActivity())
        getBundleData()
        setListener()

        return binding.root
    }

    private fun setListener() {
        binding.toolbar.ivBack.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
    }

    private fun getBundleData() {
        val name = arguments?.getString(StoreFragment.name)
        val id = arguments?.getInt(StoreFragment.id)
        val type = arguments?.getInt(StoreFragment.type)
        if (id != null) {
            if (type != null) {
                mProgressDialog.show()
                planViewModel.getPlans(id, type)
            }
        }
        binding.toolbar.tvTitle.text = name

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecycleView()
        setObserver()

    }

    private fun setRecycleView() {
        planAdapter = PlansAdapter(requireActivity(), object : PlansAdapter.PlanAdapterItemClick {
            override fun redirectToDetails(planDataItem: PlanDataItem) {
                val bundle = Bundle()
                bundle.putString("planID", planDataItem.planId.toString())
                findNavController().navigate(
                    R.id.action_planFragment_to_planDetailFragment,
                    bundle
                )
            }

            override fun redirectToPayment(planDataItem: PlanDataItem) {
                mProgressDialog.show()
                planViewModel.selectedPlanDetails = planDataItem
                planViewModel.getPlanPayment(planDataItem.price, planDataItem.planId.toString())

            }

        })
        binding.rvSimProvider.adapter = planAdapter


    }

    private fun setObserver() {

        planViewModel.planLiveData.observe(viewLifecycleOwner) { result ->
            mProgressDialog.dismiss()
            when (result) {
                is ResultWrapper.Success -> if (result.value != null) {
                    // Success code go here
                    planAdapter.submitList(result.value.data)

                }
                is ResultWrapper.Error -> {
                    Utils.showSnackBar(
                        binding.root,
                        result.errorResponse?.message!!,
                        true,
                        requireActivity()
                    )
                }
                is ResultWrapper.SessionExpired -> {
                    planViewModel.clearPreference()
                    redirectToLogin()
                }
                else -> {

                    // if result value is something else
                }
            }
        }
        planViewModel.stripLiveData.observe(viewLifecycleOwner) { result ->
            mProgressDialog.dismiss()
            when (result) {
                is ResultWrapper.Success -> if (result.value != null) {
                    // Success code go here
                    planViewModel.paymentRawDetails = result.value
                    findNavController().navigate(
                        R.id.action_planFragment_to_paymentConfirmationFragment)
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