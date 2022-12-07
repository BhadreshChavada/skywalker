package com.skywalker.ui.profile

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.skywalker.R
import com.skywalker.baseClass.BaseFragment
import com.skywalker.databinding.FragmentProfileBinding
import com.skywalker.helper.Utils
import com.skywalker.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
    }

    private fun setClickListener() {
        binding.tvAccountInformation.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeMainFragment_to_accountInfoFragment
            )
        }

        binding.tvOrders.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeMainFragment_to_orderHistoryFragment
            )
        }

        binding.tvHelpCenter.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeMainFragment_to_moreInfoFragment
            )
        }

        binding.tvMoreInfo.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeMainFragment_to_moreInfoFragment
            )
        }

        binding.tvActivatePlan.setOnClickListener {
            findNavController().navigate(
                R.id.action_mainFragment_to_cardActivationFirstStepFragment
            )
        }

        binding.tvLogout.setOnClickListener {
            logoutConfirmation()
        }

        binding.tvReferAndEarn.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeMainFragment_to_referEarnFragment
            )
        }

        binding.tvSkyMoneyAmount.setOnClickListener {
            findNavController().navigate(R.id.action_homeMainFragment_to_walletFragment)
        }

        binding.tvContactUs.setOnClickListener {
            openEmail()
        }

    }

    private fun openEmail() {
        try {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:") // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(Utils.contactUsEmail))
            intent.putExtra(Intent.EXTRA_SUBJECT, Utils.contactUsSubject)
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {

        }
    }

    private fun logoutConfirmation() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(getString(R.string.app_name))
        builder.setMessage(getString(R.string.logout_confirmation))

        builder.setPositiveButton(getString(R.string.yes)) { dialog, which ->
            dialog.dismiss()
            profileViewModel.clearPreference()
//            Handler(Looper.myLooper()!!).postDelayed({
                val intent = Intent(requireActivity(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
//            },2000)


        }

        builder.setNegativeButton(getString(R.string.no)) { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }


}