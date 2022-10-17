package com.skywalker.model.respone

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CountryDataItem(
    @SerializedName("image")
    val image: String = "",
    @SerializedName("code")
    val code: String = "",
    @SerializedName("dialCode")
    val dialCode: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("countryId")
    val countryId: Int = 0
)


data class CountryData(
    @SerializedName("data")
    val data: List<CountryDataItem>?,
    @SerializedName("meta")
    val meta: Meta
)


