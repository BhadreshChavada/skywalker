package com.skywalker.model.respone

import com.google.gson.annotations.SerializedName

data class PlanDetailResponse(
    @SerializedName("data")
    val planDataItem: PlanDataItem?,
)
