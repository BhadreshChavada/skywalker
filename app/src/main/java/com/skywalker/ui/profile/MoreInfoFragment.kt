package com.skywalker.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.skywalker.R
import com.skywalker.databinding.FragmentMoreInfoBinding

class MoreInfoFragment : Fragment(R.layout.fragment_more_info) {

    private lateinit var binding: FragmentMoreInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_more_info, container, false
        )
        binding.toolbar.tvTitle.text = getString(R.string.more_info)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
    }

    private fun setClickListener() {
        binding.tvAboutSkywalker.setOnClickListener {
          WebViewFragment.loadWebView("https://nomadinternet.com/blogs/countrynomad",
              "About us",this,
              R.id.action_moreInfoFragment_to_webViewFragment)
        }


        binding.tvPrivacyPolicy.setOnClickListener {

                WebViewFragment.loadWebView("https://nomadinternet.com/pages/privacy-policy",
                    "Privacy Policy",this,
                    R.id.action_moreInfoFragment_to_webViewFragment)

        }

        binding.tvTermsCondition.setOnClickListener {

                WebViewFragment.loadWebView("https://nomadinternet.com/policies/terms-of-service",
                    "Terms and Condition",this,
                    R.id.action_moreInfoFragment_to_webViewFragment)

        }

        binding.toolbar.ivBack.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
    }
}