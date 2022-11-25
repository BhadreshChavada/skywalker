package com.skywalker.model.request

import com.google.gson.annotations.SerializedName

data class UpdatePaymentStatusRequest(
    @SerializedName("orderId")
    val orderId: String = "",
    @SerializedName("paymentId")
    val paymentId: String = "",
    @SerializedName("status")
    val status: String = ""
)