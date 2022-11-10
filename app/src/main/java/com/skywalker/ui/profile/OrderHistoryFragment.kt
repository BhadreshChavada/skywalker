package com.skywalker.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.skywalker.R
import com.skywalker.baseClass.BaseFragment
import com.skywalker.connection.ResultWrapper
import com.skywalker.databinding.FragmentOrderHistoryBinding
import com.skywalker.helper.ApiProgressDialog
import com.skywalker.helper.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderHistoryFragment : BaseFragment(R.layout.fragment_order_history) {

    private lateinit var binding: FragmentOrderHistoryBinding
    private val mViewModel: ProfileViewModel by viewModels()
    lateinit var orderHistoryAdapter: OrderHistoryAdapter
    private lateinit var mProgressDialog: ApiProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_order_history, container, false
        )
        binding.toolbar.tvTitle.text = getString(R.string.orders)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
        setObserver()
        setRecycleview()
        mProgressDialog = ApiProgressDialog(requireActivity())
        mProgressDialog.show()
        mViewModel.getOrderHistory()
    }

    private fun setClickListener() {
        binding.toolbar.ivBack.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
    }

    private fun setRecycleview() {
        orderHistoryAdapter = OrderHistoryAdapter(requireActivity())
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