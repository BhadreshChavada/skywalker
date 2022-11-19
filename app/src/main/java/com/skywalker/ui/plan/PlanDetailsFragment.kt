package com.skywalker.ui.plan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.skywalker.R
import com.skywalker.baseClass.BaseFragment
import com.skywalker.connection.ResultWrapper
import com.skywalker.databinding.FragmentPlanDetailsBinding
import com.skywalker.helper.ApiProgressDialog
import com.skywalker.helper.Utils
import com.skywalker.model.respone.PlanDataItem
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PlanDetailsFragment : BaseFragment(R.layout.fragment_plan_details) {

    private lateinit var binding: FragmentPlanDetailsBinding

    private val planViewModel: PlanViewModel by activityViewModels()
    private lateinit var mProgressDialog: ApiProgressDialog
    lateinit var planDetail: PlanDataItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_plan_details, container, false
        )
        mProgressDialog = ApiProgressDialog(requireActivity())
        return binding.root
    }

    private fun setListener() {
        binding.toolbar.ivBack.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        binding.btnBuy.setOnClickListener {
            mProgressDialog.show()
            planViewModel.selectedPlanDetails = planDetail
            planViewModel.getPlanPayment(planDetail.price, planDetail.planId.toString())
        }
        binding.tvShowMore.setOnClickListener {
            planViewModel.selectedPlanDetails = planDetail
            findNavController().navigate(
                R.id.action_planDetailFragment_to_planAdditionalInfoFragment
            )
        }
    }

    private fun getBundleData() {
        val planID = arguments?.getString("planID")
        mProgressDialog.show()
        planViewModel.getPlansDetails(planID!!.toInt())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundleData()
        setListener()
        setObserver()
    }

    private fun setObserver() {
        planViewModel.planDetailsLiveData.observe(viewLifecycleOwner) { result ->
            mProgressDialog.dismiss()
            when (result) {
                is ResultWrapper.Success -> if (result.value != null) {
                    // Success code go here
                    planDetail = result.value.planDataItem!!
                    binding.data = planDetail
                    binding.toolbar.tvTitle.text = planDetail.title
                    if(planDetail.status == "sold"){
                        binding.btnBuy.visibility = GONE
                    }
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
                    val bundle = Bundle()
                    val paymentData = result.value.data
                    bundle.putString("ephemeralKey", paymentData.ephemeralKey)
                    bundle.putString("customer", paymentData.customer)
                    bundle.putString("publishableKey", paymentData.publishableKey)
                    bundle.putString("paymentIntent", paymentData.paymentIntent)
                    findNavController().navigate(
                        R.id.action_planDetailFragment_to_paymentConfirmationFragment,
                        bundle
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