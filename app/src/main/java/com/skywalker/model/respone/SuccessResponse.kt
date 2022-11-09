package com.skywalker.model.respone

import com.google.gson.annotations.SerializedName

data class SuccessResponse(
    @SerializedName("message")
    val message: String = ""
)