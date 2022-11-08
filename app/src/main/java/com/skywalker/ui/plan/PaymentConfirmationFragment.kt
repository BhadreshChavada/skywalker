package com.skywalker.ui.plan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.skywalker.R
import com.skywalker.databinding.FragmentPaymentConfirmationBinding
import com.skywalker.helper.ApiProgressDialog
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PaymentConfirmationFragment : Fragment(R.layout.fragment_payment_confirmation) {

    private lateinit var binding: FragmentPaymentConfirmationBinding

    private val planViewModel: PlanViewModel by viewModels()
    private lateinit var mProgressDialog: ApiProgressDialog

    private lateinit var paymentSheet: PaymentSheet
    private lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    lateinit var paymentIntentClientSecret: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_payment_confirmation, container, false
        )
        setupPayment()
        setListener()
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
                is PaymentSheetResult.Canceled -> {
                    print("Canceled")
                }
                is PaymentSheetResult.Failed -> {
                    print("Error: ${paymentSheetResult.error}")
                }
                is PaymentSheetResult.Completed -> {
                    // Display for example, an order confirmation screen
                    print("Completed")
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

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {

    }


}