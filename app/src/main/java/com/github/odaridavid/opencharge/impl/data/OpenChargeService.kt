package com.github.odaridavid.opencharge.impl.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenChargeService {

    @GET("/poi")
    suspend fun getPoiList(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("distance") distance: Int,
        @Query("distanceunit") distanceUnit: String = "km"
    ): Response<List<PoiResponse>>
}
