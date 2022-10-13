package com.skywalker.model.respone


import com.google.gson.annotations.SerializedName


data class RegionResponse(
    @SerializedName("data")
    val data: List<RegionDataItem>?,
    @SerializedName("meta")
    val meta: Meta
)


data class RegionDataItem(
    @SerializedName("image")
    val image: String = "",
    @SerializedName("regionId")
    val regionId: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("countries")
    val countries: CountryDataItem
)


