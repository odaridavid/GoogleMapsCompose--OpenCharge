package com.github.odaridavid.opencharge.impl.data

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class PoiResponse(
    @SerialName("AddressInfo") val addressInfo: AddressInfo,
    @SerialName("NumberOfPoints") val numberOfPoints: Int?
)

@kotlinx.serialization.Serializable
data class AddressInfo(
    @SerialName("Title") val title: String?,
    @SerialName("Latitude") val latitude: Double?,
    @SerialName("Longitude") val longitude: Double?,
    @SerialName("AddressLine1") val addressLineOne: String?,
    @SerialName("AddressLine2") val addressLineTwo: String?,
    @SerialName("Town") val town: String?,
    @SerialName("Postcode") val postcode: String?
)
