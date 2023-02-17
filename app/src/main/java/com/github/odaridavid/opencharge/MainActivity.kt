package com.github.odaridavid.opencharge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.github.odaridavid.opencharge.presentation.*
import com.github.odaridavid.opencharge.theme.OpenChargeTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mapViewModel: MapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenChargeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AppNavGraph(
                        navController = rememberNavController(),
                        mapViewModel = mapViewModel,
                        poiDetailsViewModel = viewModel()
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapViewModel.processIntent(MapScreenIntent.LoadPoiData)
    }

    override fun onPause() {
        super.onPause()
        mapViewModel.processIntent(MapScreenIntent.StopPoiDataRefresh)
    }

}
