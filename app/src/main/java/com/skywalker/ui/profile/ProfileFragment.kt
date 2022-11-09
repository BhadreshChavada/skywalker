package com.skywalker.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.ViewBindingAdapter.setClickListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.skywalker.R
import com.skywalker.databinding.FragmentProfileBinding
import com.skywalker.databinding.FragmentStoreBinding
import com.skywalker.helper.ApiProgressDialog
import com.skywalker.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

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

        binding.tvLogout.setOnClickListener {
            logoutConfirmation()
        }

    }

    fun logoutConfirmation(){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(getString(R.string.app_name))
        builder.setMessage(getString(R.string.logout_confirmation))
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            Toast.makeText(requireActivity(),
                android.R.string.yes, Toast.LENGTH_SHORT).show()

            redirectToLogin()
            dialog.dismiss()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            Toast.makeText(requireActivity(),
                android.R.string.no, Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        builder.show()
    }

    private fun redirectToLogin(){
        profileViewModel.clearPreference()
        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}