package com.skywalker.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
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

    private val RC_SIGN_IN: Int = 100
    private lateinit var binding: FragmentAuthenticationBinding
    private lateinit var referrerClient: InstallReferrerClient

    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    private lateinit var mProgressDialog: ApiProgressDialog
    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configGoogleSignIn()
    }

    private fun configGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val account = GoogleSignIn.getLastSignedInAccount(requireActivity())
//        updateUI(account)
    }

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
        authenticationViewModel.isFreshInstalled()
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

        authenticationViewModel.isFreshInstalled.observe(viewLifecycleOwner) {
            if (it) {
                initReferral()
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
                    result.errorResponse?.message?.let {
                        Utils.showSnackBar(
                            binding.root,
                            it, true, requireActivity()
                        )
                    }
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

        binding.btnGoogleSignin.setOnClickListener {
            googleSignIn()
        }

        binding.btnGoogleSignUp.setOnClickListener {
            googleSignIn()
        }
    }

    private fun initReferral() {
        referrerClient = InstallReferrerClient.newBuilder(requireActivity()).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {

            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        // Connection established.
                        val response: ReferrerDetails = referrerClient.installReferrer
                        val referrerUrl: String = response.installReferrer
                        val referrerClickTime: Long = response.referrerClickTimestampSeconds
                        val appInstallTime: Long = response.installBeginTimestampSeconds
                        val instantExperienceLaunched: Boolean = response.googlePlayInstantParam
                        referrerClient.endConnection()
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        // API not available on the current Play Store app.
                    }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        // Connection couldn't be established.
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    private fun googleSignIn() {
        val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.

            authenticationViewModel.socialSignupRequest.providerId = account.id.toString()
            authenticationViewModel.socialSignupRequest.providerToken = account.idToken.toString()
            authenticationViewModel.socialSignupRequest.providerType = "google"
            authenticationViewModel.doSocialSignUp()
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("GoogleSignIn", e.message.toString())
            Utils.showSnackBar(
                binding.root,
                "signIn failed ", true, requireActivity()
            )

        }
    }

}