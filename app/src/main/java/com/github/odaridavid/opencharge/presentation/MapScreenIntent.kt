package com.github.odaridavid.opencharge.presentation

sealed class MapScreenIntent {
    object LoadPoiData : MapScreenIntent()
    object StopPoiDataRefresh : MapScreenIntent()
}
