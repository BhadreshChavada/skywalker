package com.skywalker.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.skywalker.R
import com.skywalker.databinding.FragmentAboutSkywalkerBinding
import com.skywalker.databinding.FragmentAccountInformationBinding
import com.skywalker.ui.store.StoreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountInformationFragment : Fragment(R.layout.fragment_account_information) {

    private lateinit var binding: FragmentAccountInformationBinding
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_account_information, container, false
        )
        binding.toolbar.tvTitle.text = getString(R.string.account_information)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
        profileViewModel.getUserData()
        setObserver()
    }

    private fun setClickListener() {
        binding.toolbar.ivBack.setOnClickListener { requireActivity().onBackPressed() }
    }

    private fun setObserver() {
        profileViewModel.userLiveData.observe(viewLifecycleOwner) {
            binding.data = it
        }
    }
}