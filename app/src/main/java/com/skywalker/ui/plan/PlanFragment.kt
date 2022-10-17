package com.skywalker.ui.plan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.ViewGroupBindingAdapter.setListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.skywalker.R
import com.skywalker.connection.ResultWrapper
import com.skywalker.databinding.FragmentCountryWiseSimProviderBinding
import com.skywalker.databinding.FragmentStoreBinding
import com.skywalker.helper.ApiProgressDialog
import com.skywalker.model.respone.CountryDataItem
import com.skywalker.ui.store.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PlanFragment : Fragment(R.layout.fragment_country_wise_sim_provider) {

    private lateinit var binding: FragmentCountryWiseSimProviderBinding

    private val planViewModel: PlanViewModel by viewModels()
    private lateinit var mProgressDialog: ApiProgressDialog

    lateinit var planAdapter: PlansAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_country_wise_sim_provider, container, false
        )
        getBundleData()
        setListener()
        mProgressDialog = ApiProgressDialog(requireActivity())
        return binding.root
    }

    private fun setListener() {
        binding.toolbar.ivBack.setOnClickListener { requireActivity().onBackPressed() }
    }

    private fun getBundleData() {
        val name = arguments?.getString(StoreFragment.name)
        val id = arguments?.getInt(StoreFragment.id)
        val type = arguments?.getInt(StoreFragment.type)
        if (id != null) {
            if (type != null) {
                planViewModel.getPlans(id,type)
            }
        }
        binding.toolbar.tvTitle.text = name

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mProgressDialog.show()
        setRecycleView()
        setObserver()

    }

    private fun setRecycleView() {
        planAdapter = PlansAdapter(requireActivity())
        binding.rvSimProvider.adapter = planAdapter


    }

    private fun setObserver() {

        planViewModel.planLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultWrapper.Success -> if (result.value != null) {
                    // Success code go here
                    mProgressDialog.dismiss()

                    planAdapter.submitList(result.value.data)

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

    }


}