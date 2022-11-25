package com.skywalker.model.respone

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StripData(
    @SerializedName("data")
    val data: Data
)


data class Data(
    @SerializedName("publishableKey")
    val publishableKey: String = "",
    @SerializedName("ephemeralKey")
    val ephemeralKey: String = "",
    @SerializedName("paymentIntent")
    val paymentIntent: String = "",
    @SerializedName("customer")
    val customer: String = "",
    @SerializedName("orderId")
    val orderId: String = "",
    @SerializedName("paymentId")
    val paymentId: String = ""
) : Serializable


