package com.github.odaridavid.opencharge.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.odaridavid.opencharge.api.Poi
import com.github.odaridavid.opencharge.api.PoiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val poiRepository: PoiRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MapViewState(isLoading = true))
    val state: StateFlow<MapViewState> = _state

    fun processIntent(mapScreenIntent: MapScreenIntent) {
        when (mapScreenIntent) {
            is MapScreenIntent.LoadPoiData -> {
                viewModelScope.launch {
                    poiRepository.retrievePois().collect { result ->
                        processResult(result)
                    }
                }
            }
            is MapScreenIntent.StopPoiDataRefresh -> {
                poiRepository.cancelRepeatingTransactions()
            }
        }
    }

    private fun processResult(result: Result<List<Poi>>) {
        when {
            result.isSuccess -> {
                val poiData = result.getOrNull()
                setState {
                    copy(
                        pois = poiData ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
            }
            result.isFailure -> {
                setState {
                    copy(
                        isLoading = false,
                        error = result.exceptionOrNull()
                            ?: Throwable("Unknown error occurred")
                    )
                }
            }
        }
    }

    private fun setState(stateReducer: MapViewState.() -> MapViewState) {
        viewModelScope.launch {
            _state.emit(stateReducer(state.value))
        }
    }
}

data class MapViewState(
    val pois: List<Poi> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null
)
