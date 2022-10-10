package com.skywalker.model.respone


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data")
    val data: Data,
    @SerializedName("message")
    val message: String = ""
)


data class Authentication(
    @SerializedName("accessToken")
    val accessToken: String = "",
    @SerializedName("expiresAt")
    val expiresAt: Int = 0,
    @SerializedName("refreshToken")
    val refreshToken: String = ""
)


data class Data(
    @SerializedName("firstName")
    val firstName: String = "",
    @SerializedName("lastName")
    val lastName: String = "",
    @SerializedName("isBlock")
    val isBlock: Boolean = false,
    @SerializedName("joinedAt")
    val joinedAt: Int = 0,
    @SerializedName("referralCode")
    val referralCode: String = "",
    @SerializedName("userName")
    val userName: String = "",
    @SerializedName("userId")
    val userId: Int = 0,
    @SerializedName("email")
    val email: String = "",
    @SerializedName("authentication")
    val authentication: Authentication
)


