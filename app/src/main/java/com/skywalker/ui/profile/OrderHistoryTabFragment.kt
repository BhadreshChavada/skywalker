package com.skywalker.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.ViewBindingAdapter.setClickListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.skywalker.R
import com.skywalker.baseClass.BaseFragment
import com.skywalker.connection.ResultWrapper
import com.skywalker.databinding.FragmentOrderHistoryBinding
import com.skywalker.databinding.FragmentOrderMainTabHistoryBinding
import com.skywalker.helper.ApiProgressDialog
import com.skywalker.helper.Utils
import com.skywalker.model.respone.PlanDataItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderHistoryTabFragment : BaseFragment(R.layout.fragment_order_main_tab_history) {

    private lateinit var binding: FragmentOrderMainTabHistoryBinding
    private val mViewModel: ProfileViewModel by viewModels()
    lateinit var orderHistoryAdapter: OrderHistoryAdapter
    private lateinit var mProgressDialog: ApiProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_order_main_tab_history, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserver()
        setRecycleview()
        mProgressDialog = ApiProgressDialog(requireActivity())
        mProgressDialog.show()
        mViewModel.getOrderHistory()
    }


    private fun setRecycleview() {
        orderHistoryAdapter = OrderHistoryAdapter(requireActivity(),object : OrderHistoryAdapter.ItemClick{
            override fun redirectToDetails(planDataItem: PlanDataItem) {
                val bundle = Bundle()
                bundle.putString("planID", planDataItem.planId.toString())
                bundle.putBoolean("isPurchasedSim", true)
                findNavController().navigate(
                    R.id.action_mainFragmentt_to_planDetailFragment,
                    bundle
                )
            }

        })
        binding.rvOrder.adapter = orderHistoryAdapter
    }

    private fun setObserver() {
        mViewModel.orderHistoryLiveData.observe(viewLifecycleOwner) { result ->
            mProgressDialog.dismiss()
            when (result) {
                is ResultWrapper.Success -> if (result.value != null) {
                    // Success code go here
                    orderHistoryAdapter.submitList(result.value.data)

                    if (orderHistoryAdapter.itemCount > 0) {
                        binding.tvDescription.visibility = GONE
                        binding.tvTitle.visibility = GONE
                        binding.ivNoOrder.visibility = GONE
                        binding.rvOrder.visibility = VISIBLE
                    } else {
                        binding.tvDescription.visibility = VISIBLE
                        binding.tvTitle.visibility = VISIBLE
                        binding.ivNoOrder.visibility = VISIBLE
                        binding.rvOrder.visibility = GONE
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
                    mViewModel.clearPreference()
                    redirectToLogin()
                }
                else -> {

                    // if result value is something else
                }
            }
        }
    }
}