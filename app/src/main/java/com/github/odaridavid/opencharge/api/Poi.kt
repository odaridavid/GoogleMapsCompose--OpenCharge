package com.github.odaridavid.opencharge.api

data class Poi(
    val title: String,
    val noOfChargingPoints: Int,
    val address: Address
)

data class Address(
    val addressLineOne: String,
    val addressLineTwo: String,
    val postCode: String,
    val latitude: Double?,
    val longitude: Double?
)
