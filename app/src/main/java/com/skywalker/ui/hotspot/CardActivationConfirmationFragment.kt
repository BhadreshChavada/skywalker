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
import com.skywalker.databinding.FragmentHotspotActivationConfirmationBinding
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
class CardActivationConfirmationFragment :
    BaseFragment(R.layout.fragment_hotspot_activation_confirmation) {

    private lateinit var binding: FragmentHotspotActivationConfirmationBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_hotspot_activation_confirmation, container, false
        )
        binding.toolbar.tvTitle.text = "Active Sim"
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
        init()
    }

    private fun init() {
        binding.btnBuy.setOnClickListener {
            val simId = arguments?.getString("simId")
            WebViewFragment.loadWebView(
                "https://np.nomadinternet.com/np-activation.php?iccid=$simId",
                "Paynow", this,
                R.id.action_cardActivation_to_webViewFragment
            )
        }


    }

    private fun setClickListener() {
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }


}