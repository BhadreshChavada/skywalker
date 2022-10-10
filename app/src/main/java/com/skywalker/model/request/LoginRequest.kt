package com.skywalker.model.request

data class LoginRequest(
    var password: String = "",
    var email: String = ""
)