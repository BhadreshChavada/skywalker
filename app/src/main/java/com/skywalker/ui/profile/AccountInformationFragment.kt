package com.skywalker.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.skywalker.R
import com.skywalker.baseClass.BaseFragment
import com.skywalker.connection.ResultWrapper
import com.skywalker.databinding.FragmentAccountInformationBinding
import com.skywalker.helper.ApiProgressDialog
import com.skywalker.helper.Utils
import com.skywalker.model.request.UpdateProfileRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountInformationFragment : BaseFragment(R.layout.fragment_account_information) {

    private lateinit var binding: FragmentAccountInformationBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var mProgressDialog: ApiProgressDialog
    private var isUpdateCall = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_account_information, container, false
        )
        mProgressDialog = ApiProgressDialog(requireActivity())
        binding.toolbar.tvTitle.text = getString(R.string.account_information)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
        mProgressDialog.show()
        profileViewModel.getUserData()
        setObserver()
    }

    private fun setClickListener() {
        binding.toolbar.ivBack.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }

        binding.btnRegister.setOnClickListener {
            isUpdateCall = true
            mProgressDialog.show()
            profileViewModel.updateProfile(
                UpdateProfileRequest(
                    binding.edtFirstName.text.toString(),
                    binding.edtLastName.text.toString(),
                    binding.edtEmail.text.toString()
                )
            )
        }
    }

    private fun setObserver() {
        profileViewModel.userLiveData.observe(viewLifecycleOwner) { result ->
            mProgressDialog.dismiss()
            when (result) {
                is ResultWrapper.Success -> if (result.value != null) {
                    // Success code go here
                    binding.data = result.value.data
                    if(isUpdateCall){
                        isUpdateCall = false
                        Utils.showSnackBar(
                            binding.root,
                            getString(R.string.profile_update_successfully),
                            false,
                            requireActivity()
                        )
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                }
                is ResultWrapper.Error -> {
                    Utils.showSnackBar(
                        binding.root,
                        result.errorResponse?.message!!,
                        true,
                        requireActivity()
                    )
                }
                is ResultWrapper.SessionExpired -> {
                    profileViewModel.clearPreference()
                    redirectToLogin()
                }
                else -> {

                    // if result value is something else
                }
            }
        }
    }
}