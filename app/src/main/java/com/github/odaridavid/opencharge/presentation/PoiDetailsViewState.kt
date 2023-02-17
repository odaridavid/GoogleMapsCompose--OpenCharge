package com.github.odaridavid.opencharge.presentation

sealed class PoiDetailsIntent {
    data class LoadPoiData(val poiTitle: String) : PoiDetailsIntent()
}
