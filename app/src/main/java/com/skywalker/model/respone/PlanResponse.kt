package com.skywalker.model.respone


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlanDataItem(
    @SerializedName("country")
    val country: Country,
    @SerializedName("data")
    val data: String = "",
    @SerializedName("price")
    val price: String = "",
    @SerializedName("planId")
    val planId: Int = 0,
    @SerializedName("theme")
    val theme: Int = 0,
    @SerializedName("validity")
    val validity: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("type")
    val type: Int = 0,
    @SerializedName("region")
    val region: Region
) : Parcelable


data class PlanResponse(
    @SerializedName("data")
    val data: List<PlanDataItem>?,
    @SerializedName("meta")
    val meta: Meta
)


@Parcelize
data class Country(
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
) : Parcelable


@Parcelize
data class Region(
    @SerializedName("image")
    val image: String = "",
    @SerializedName("regionId")
    val regionId: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("countries")
    val countries: Country
) : Parcelable


