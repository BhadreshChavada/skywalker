package com.skywalker.model.request

import com.google.gson.annotations.SerializedName

data class SignupRequest(
    @SerializedName("password")
    var password: String = "",
    @SerializedName("userName")
    var userName: String = "",
    @SerializedName("email")
    var email: String = "",
    @Transient
    @SerializedName("confirmPassword")
    var confirmPassword: String = "",
    @SerializedName("referralCode")
    var referralCode: String = "",
)