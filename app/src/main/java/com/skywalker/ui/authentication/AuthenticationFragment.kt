package com.skywalker.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.skywalker.R
import com.skywalker.connection.ResultWrapper
import com.skywalker.databinding.FragmentAuthenticationBinding
import com.skywalker.helper.ApiProgressDialog
import com.skywalker.helper.Utils
import com.skywalker.model.request.SignupRequest
import com.skywalker.ui.homeTab.MainTabActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationFragment : Fragment(R.layout.fragment_authentication) {

    private lateinit var binding: FragmentAuthenticationBinding

    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    private lateinit var mProgressDialog: ApiProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_authentication, container, false
        )
        mProgressDialog = ApiProgressDialog(requireActivity())
        binding.viewModel = authenticationViewModel
        authenticationViewModel.updateWTStatus()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        setObserver()
    }

    private fun setObserver() {
        authenticationViewModel.showErrorLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                Utils.showSnackBar(binding.root, it, true, requireActivity())
                mProgressDialog.dismiss()
            }
        }

        authenticationViewModel.mSignUpData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultWrapper.Success -> if (result.value != null) {
                    // Success code go here
                    mProgressDialog.dismiss()
                    Utils.showSnackBar(binding.root, result.value.message, false, requireActivity())
                    authenticationViewModel.signupRequest = SignupRequest("", "", "", "")
                    binding.tvLogin.performClick()
                    binding.viewModel = authenticationViewModel

                }
                is ResultWrapper.Error -> {
                    mProgressDialog.dismiss()
                }
                else -> {
                    mProgressDialog.dismiss()
                    // if result value is something else
                }
            }
        }

        authenticationViewModel.mLoginData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultWrapper.Success -> if (result.value != null) {
                    // Success code go here
                    mProgressDialog.dismiss()
                    authenticationViewModel.saveUserData(result.value.data)
                    result.value?.message?.let {
                        Utils.showSnackBar(
                            binding.root,
                            it, false, requireActivity()
                        )
                    }
                    redirectToHomeScreen()
                }
                is ResultWrapper.Error -> {
                    mProgressDialog.dismiss()
                    result.errorResponse?.message?.let {
                        Utils.showSnackBar(
                            binding.root,
                            it, true, requireActivity()
                        )
                    }

                }
                else -> {
                    // if result value is something else
                    mProgressDialog.dismiss()
                }
            }
        }
    }

    private fun redirectToHomeScreen() {
        val intent = Intent(requireActivity(), MainTabActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }


    private fun setListener() {
        binding.tvSignUp.setOnClickListener {
            binding.tvSignUp.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_rectangle_rounded_corner_green))
            binding.tvLogin.setBackgroundDrawable(null)
            binding.tvSignUp.setTextColor(resources.getColor(R.color.white))
            binding.tvLogin.setTextColor(resources.getColor(R.color.black_text))
            binding.cardSignup.visibility = VISIBLE
            binding.cardSignin.visibility = GONE
        }

        binding.tvLogin.setOnClickListener {
            binding.tvLogin.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_rectangle_rounded_corner_green))
            binding.tvSignUp.setBackgroundDrawable(null)
            binding.tvLogin.setTextColor(resources.getColor(R.color.white))
            binding.tvSignUp.setTextColor(resources.getColor(R.color.black_text))
            binding.cardSignin.visibility = VISIBLE
            binding.cardSignup.visibility = GONE
        }

        binding.btnSignIn.setOnClickListener {
            mProgressDialog.show()
            authenticationViewModel.doLogin()
        }

        binding.btnRegister.setOnClickListener {
            mProgressDialog.show()
            authenticationViewModel.doSignUp()
        }
    }

}