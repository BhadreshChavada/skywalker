package com.skywalker.model.respone

import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("total")
    val total: Int = 0,
    @SerializedName("perPage")
    val perPage: Int = 0,
    @SerializedName("totalPage")
    val totalPage: Int = 0,
    @SerializedName("currentPage")
    val currentPage: Int = 0
)