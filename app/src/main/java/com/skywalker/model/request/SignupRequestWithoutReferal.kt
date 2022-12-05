package com.skywalker.model.request

import com.google.gson.annotations.SerializedName

data class SignupRequestWithoutReferal(
    @SerializedName("password")
    var password: String = "",
    @SerializedName("userName")
    var userName: String = "",
    @SerializedName("email")
    var email: String = "",
)