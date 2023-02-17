package com.github.odaridavid.opencharge.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.odaridavid.opencharge.api.Poi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

private const val DEFAULT_LATITUDE = 52.526
private const val DEFAULT_LONGITUDE = 13.415
private const val DEFAULT_ZOOM = 15f

@Composable
fun MapScreen(state: MapViewState, onPoiClick: (Poi) -> Unit) {
    val defaultLocation = LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, DEFAULT_ZOOM)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM))
        for (poi in state.pois) {
            val latitude = poi.address.latitude
            val longitude = poi.address.longitude
            if (latitude != null && longitude != null) {
                val latlng = LatLng(latitude, longitude)
                Marker(
                    state = MarkerState(position = latlng),
                    title = poi.title,
                    snippet = poi.address.addressLineOne,
                    onClick = {
                        onPoiClick(poi)
                        false
                    }
                )
            }
        }
    }
}
