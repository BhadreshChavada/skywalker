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
import com.chargebee.android.billingservice.CBCallback
import com.chargebee.android.billingservice.CBPurchase
import com.chargebee.android.billingservice.PurchaseModel
import com.chargebee.android.exceptions.CBException
import com.chargebee.android.models.CBProduct
import com.chargebee.android.network.ReceiptDetail
import com.skywalker.R
import com.skywalker.baseClass.BaseFragment
import com.skywalker.connection.ResultWrapper
import com.skywalker.databinding.FragmentActivateDeviceStepTwoBinding
import com.skywalker.databinding.FragmentHotspotDetailBinding
import com.skywalker.databinding.FragmentHotspotHomeBinding
import com.skywalker.databinding.FragmentStoreBinding
import com.skywalker.helper.AdapterItemClickListener
import com.skywalker.helper.ApiProgressDialog
import com.skywalker.helper.Utils
import com.skywalker.model.respone.CountryDataItem
import com.skywalker.model.respone.PlanDataItem
import com.skywalker.model.respone.RegionDataItem
import com.skywalker.ui.plan.PlanViewModel
import com.skywalker.ui.plan.PlansAdapter
import com.skywalker.ui.profile.WebViewFragment
import com.skywalker.ui.store.CountryAdapter
import com.skywalker.ui.store.PopularCountryAdapter
import com.skywalker.ui.store.RegionAdapter
import com.skywalker.ui.store.StoreFragment.Companion.id
import com.skywalker.ui.store.StoreFragment.Companion.name
import com.skywalker.ui.store.StoreViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CardActivationSecondStepFragment : BaseFragment(R.layout.fragment_activate_device_step_two) {

    private lateinit var binding: FragmentActivateDeviceStepTwoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_activate_device_step_two, container, false
        )
        binding.toolbar.tvTitle.text = "Active Sim"
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListener()
    }

    private fun setClickListener() {
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnYes.setOnClickListener {
            if (binding.edtSimId.text.toString().length == 20) {
                /*val bundle = Bundle()
                bundle.putString("simId", binding.edtSimId.text.toString())
                findNavController().navigate(
                    R.id.action_CardActivationSecondStepFragment_to_CardActivationConfirmationFragment,
                    bundle
                )*/

                val simId = binding.edtSimId.text.toString()
                WebViewFragment.loadWebView(
                    "https://np.nomadinternet.com/np-activation.php?iccid=$simId",
                    "Paynow", this,
                    R.id.action_cardActivationSecondStepFragment_to_webViewFragment
                )
            } else
                Utils.showSnackBar(
                    binding.root,
                    "Please enter valid sim id",
                    true,
                    requireActivity()
                )
        }
    }


}