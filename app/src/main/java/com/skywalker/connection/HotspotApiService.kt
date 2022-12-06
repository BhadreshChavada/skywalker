package com.skywalker.connection

import com.skywalker.model.request.*
import com.skywalker.model.respone.*
import retrofit2.Response
import retrofit2.http.*


interface HotspotApiService {

    @GET("home_page_products.json")
    suspend fun getHotSpotData(): Response<List<HotspotDetails>>
}