package com.skywalker.ui.plan

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.skywalker.R
import com.skywalker.baseClass.BaseFragment
import com.skywalker.connection.ResultWrapper
import com.skywalker.databinding.FragmentPaymentConfirmationBinding
import com.skywalker.helper.ApiProgressDialog
import com.skywalker.helper.Utils
import com.skywalker.model.request.UpdatePaymentStatusRequest
import com.skywalker.model.respone.PlanDataItem
import com.skywalker.ui.homeTab.MainTabActivity
import com.skywalker.ui.main.MainActivity
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PaymentConfirmationFragment : BaseFragment(R.layout.fragment_payment_confirmation) {

    private lateinit var binding: FragmentPaymentConfirmationBinding

    private val planViewModel: PlanViewModel by activityViewModels()
    private lateinit var mProgressDialog: ApiProgressDialog

    private lateinit var paymentSheet: PaymentSheet
    private lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    lateinit var paymentIntentClientSecret: String
    lateinit var planDetails: PlanDataItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_payment_confirmation, container, false
        )

        planDetails = planViewModel.selectedPlanDetails
        binding.data = planDetails
        binding.toolbar.tvTitle.text = getString(R.string.payment)
        setupPayment()
        setListener()
        setObserver()
        mProgressDialog = ApiProgressDialog(requireActivity())


        return binding.root
    }


    private fun setupPayment() {

        val ephemeralKey = arguments?.getString("ephemeralKey", "")
        val customer = arguments?.getString("customer", "")
        val publishableKey = arguments?.getString("publishableKey", "")
        paymentIntentClientSecret = arguments?.getString("paymentIntent", "")!!

        paymentSheet = PaymentSheet(
            this
        ) { paymentSheetResult ->
            when (paymentSheetResult) {
                is PaymentSheetResult.Failed -> {
                    print("Error: ${paymentSheetResult.error}")
                    planViewModel.updatePaymentStatus(
                        UpdatePaymentStatusRequest(
                            planDetails.planId,
                            "na",
                            paymentSheetResult.error.toString()
                        )
                    )
                    Utils.showSnackBar(
                        binding.root,
                        getString(R.string.payment_failed),
                        true,
                        requireActivity()
                    )
                }
                is PaymentSheetResult.Completed -> {
                    // Display for example, an order confirmation screen
                    Utils.showSnackBar(
                        binding.root,
                        getString(R.string.payment_received),
                        false,
                        requireActivity()
                    )
                    planViewModel.updatePaymentStatus(
                        UpdatePaymentStatusRequest(
                            planDetails.planId,
                            "na",
                            "succeeded"
                        )
                    )

                }
                PaymentSheetResult.Canceled -> {
                    Utils.showSnackBar(
                        binding.root,
                        getString(R.string.payment_canceled),
                        false,
                        requireActivity()
                    )
                }
            }
        }
        customerConfig = PaymentSheet.CustomerConfiguration(
            customer!!,
            ephemeralKey!!
        )
        PaymentConfiguration.init(requireActivity(), publishableKey!!)
        binding.btnBuy.isEnabled = true
    }

    private fun setListener() {
        binding.toolbar.ivBack.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        binding.btnBuy.setOnClickListener {
            presentPaymentSheet()
        }
    }

    private fun setObserver() {
        planViewModel.paymentStatusLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultWrapper.Success -> if (result.value != null) {
                    // Success code go here
                    Utils.showSnackBar(
                        binding.root,
                        result.value.message,
                        false,
                        requireActivity()
                    )
                    redirectToHome()

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
                    planViewModel.clearPreference()
                    redirectToLogin()
                }
                else -> {

                    // if result value is something else
                }
            }
        }
    }

    private fun redirectToHome() {
        Handler(Looper.myLooper()!!).postDelayed({
            val intent = Intent(requireActivity(), MainTabActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        },2000)

    }

    private fun presentPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret,
            PaymentSheet.Configuration(
                merchantDisplayName = "My merchant name",
                customer = customerConfig,
                // Set `allowsDelayedPaymentMethods` to true if your business
                // can handle payment methods that complete payment after a delay, like SEPA Debit and Sofort.
                allowsDelayedPaymentMethods = true
            )
        )
    }


}