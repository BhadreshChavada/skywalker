package com.skywalker.model.request

import com.google.gson.annotations.SerializedName

data class SocialLoginRequest(
    @SerializedName("providerToken")
    var providerToken: String = "",
    @SerializedName("providerId")
    var providerId: String = "",
    @SerializedName("providerType")
    var providerType: String = ""
)