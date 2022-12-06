package com.skywalker.model.respone

import com.google.gson.annotations.SerializedName

data class HotspotDetails(
    @SerializedName("cover")
    val cover: String = "",
    @SerializedName("images")
    val images: List<String>?,
    @SerializedName("price")
    val price: Int = 0,
    @SerializedName("description")
    val description: String = "",
    @SerializedName("theme")
    val theme: String = "",
    @SerializedName("title")
    val title: String = ""
)