package com.skywalker.ui.profile

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.skywalker.R
import com.skywalker.baseClass.BaseFragment
import com.skywalker.databinding.FragmentReferEarnBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReferEarnFragment : BaseFragment(com.skywalker.R.layout.fragment_refer_earn) {

    private lateinit var binding: FragmentReferEarnBinding

    private val viewModel: ProfileViewModel by viewModels()
    private var referralCode =""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_refer_earn, container, false
        )

        binding.toolbar.tvTitle.text=getString(R.string.refer_and_earn)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
        viewModel.getUserReferralCode()
        setObserver()
    }

    private fun setObserver(){
        viewModel.userSPLiveData.observe(viewLifecycleOwner) {
            referralCode = it.referralCode
            binding.tvReferralCode.text = referralCode
        }
    }

    private fun setClickListener() {
        binding.btnShareNow.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val shareBody = shareMessage()
            intent.type = "text/plain"
            intent.putExtra(
            Intent.EXTRA_SUBJECT,
            getString(R.string.app_name)
        )
            intent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(intent, getString(R.string.app_name)))
        }
        binding.ivCopy.setOnClickListener {
            val clipBoard =
                requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData =
                ClipData.newPlainText(getString(R.string.app_name), shareMessage())
            clipBoard.setPrimaryClip(clipData)
        }
        binding.toolbar.ivBack.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
    }

    private fun shareMessage(): String {
        return "Get US\$3 credit for evey friend that signs up and completes a purchase. Your friends get US\$3 off their purchase. \n Referal code :"+referralCode
    }
}