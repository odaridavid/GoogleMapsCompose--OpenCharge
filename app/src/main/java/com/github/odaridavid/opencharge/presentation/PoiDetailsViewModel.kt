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
class PoiDetailsViewModel @Inject constructor(
    private val poiRepository: PoiRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PoiDetailsViewState(isLoading = true))
    val state: StateFlow<PoiDetailsViewState> = _state


    fun processIntent(poiDetailsIntent: PoiDetailsIntent) {
        when (poiDetailsIntent) {
            is PoiDetailsIntent.LoadPoiData -> {
                viewModelScope.launch {
                    poiRepository.retrievePois().collect { result ->
                        processResult(result = result, title = poiDetailsIntent.poiTitle)
                    }
                }
            }
        }
    }

    private fun processResult(result: Result<List<Poi>>, title: String) {
        when {
            result.isSuccess -> {
                val poiData = result.getOrNull()
                val poi = poiData?.first { it.title == title }
                setState {
                    copy(
                        poi = poi,
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

    private fun setState(stateReducer: PoiDetailsViewState.() -> PoiDetailsViewState) {
        viewModelScope.launch {
            _state.emit(stateReducer(state.value))
        }
    }

}

data class PoiDetailsViewState(
    val poi: Poi? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null
)
