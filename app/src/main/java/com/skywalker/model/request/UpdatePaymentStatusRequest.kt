package com.skywalker.model.request

import com.google.gson.annotations.SerializedName

data class UpdatePaymentStatusRequest(
    @SerializedName("orderId")
    val orderId: Int = 0,
    @SerializedName("paymentId")
    val paymentId: String = "1",
    @SerializedName("status")
    val status: String = ""
)