package com.skywalker.model.request

import com.google.gson.annotations.SerializedName

data class PlanPaymentRequest(
    @SerializedName("amount")
    val amount: String = "0",
    @SerializedName("planId")
    val planId: String = "0",
    @SerializedName("type")
    val type: String = "mobile"
)