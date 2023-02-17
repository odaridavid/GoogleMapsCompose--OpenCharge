package com.github.odaridavid.opencharge

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.odaridavid.opencharge.presentation.*

private const val MAPS_DESTINATION = "maps"
private const val POI_DETAILS_DESTINATION = "poi-details"
private const val POI_DETAILS_ARGS = "/{title}"

@Composable
fun AppNavGraph(
    navController: NavHostController,
    mapViewModel: MapViewModel,
    poiDetailsViewModel: PoiDetailsViewModel
) {
    NavHost(navController = navController, startDestination = MAPS_DESTINATION) {
        composable(MAPS_DESTINATION) {
            MapScreen(state = mapViewModel.state.collectAsState(initial = MapViewState()).value) { poi ->
                navController.navigate("${POI_DETAILS_DESTINATION}/${poi.title}")
            }
        }
        composable(POI_DETAILS_DESTINATION + POI_DETAILS_ARGS) { backStackEntry ->
            PoiDetailsScreen(state = poiDetailsViewModel.state.collectAsState(initial = PoiDetailsViewState()).value) {
                navController.popBackStack()
            }
            val title = backStackEntry.arguments?.getString("title") ?: "-"
            poiDetailsViewModel.processIntent(PoiDetailsIntent.LoadPoiData(title))
        }
    }
}
