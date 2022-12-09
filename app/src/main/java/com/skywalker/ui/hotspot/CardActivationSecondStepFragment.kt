package com.skywalker.ui.hotspot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.skywalker.R
import com.skywalker.baseClass.BaseFragment
import com.skywalker.databinding.FragmentActivateDeviceStepTwoBinding
import com.skywalker.helper.Utils
import com.skywalker.ui.profile.WebViewFragment
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
                    Utils.SUBSCRIPTION_URL+simId,
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