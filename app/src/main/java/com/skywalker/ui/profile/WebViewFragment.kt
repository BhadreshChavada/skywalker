package com.skywalker.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.skywalker.R
import com.skywalker.baseClass.BaseFragment
import com.skywalker.databinding.FragmentWebviewBinding
import com.skywalker.ui.hotspot.MyJavaScriptChromeClient
import com.skywalker.ui.hotspot.WebViewEvents

class WebViewFragment : BaseFragment(R.layout.fragment_webview) {

    private lateinit var binding: FragmentWebviewBinding

    companion object {
        fun loadWebView(webURL: String, title: String, fragment: Fragment, action: Int) {
            val bundle = Bundle()
            bundle.putString("webURL", webURL)
            bundle.putString("title", title)
            findNavController(fragment).navigate(
                action,
                bundle
            )
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_webview, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
        loadWeb()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWeb() {
        val webURL = arguments?.getString("webURL")
        val title = arguments?.getString("title")
        binding.toolbar.tvTitle.text = title
        binding.webview.webViewClient = WebViewClient()
        binding.webview.setClickable(true)
        val wSettings = binding.webview.getSettings()
        wSettings.setJavaScriptEnabled(true)
//        val webViewClient = WebClientClass(requireActivity())
//        binding.webview.setWebViewClient(webViewClient)
        binding.webview.settings.setSupportZoom(true)
        webURL?.let { binding.webview.loadUrl(it) }
        if (title.equals("Paynow")) {

            binding.webview.addJavascriptInterface(
                MyJavaScriptChromeClient(requireActivity(), object : WebViewEvents {
                    override fun closeWebview(status: String) {
                        when (status) {
                            "FAIL" -> {
                                com.skywalker.helper.Utils.showSnackBar(
                                    binding.root,
                                    "Payment failed!", true, requireActivity()
                                )
                                redirectToHome()
                            }
                            "DONE" -> {
                                com.skywalker.helper.Utils.showSnackBar(
                                    binding.root,
                                    "Payment Successfully!", false, requireActivity()
                                )
                                redirectToHome()
                            }
                        }
                    }

                }),
                "AndroidBridge"
            )
        }


    }

    private fun redirectToHome() {
        findNavController(this).navigate(R.id.action_webViewFragment_to_homeMainFragment)
    }

    private fun setClickListener() {

        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}