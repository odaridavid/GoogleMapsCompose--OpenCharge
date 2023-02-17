package com.github.odaridavid.opencharge

import app.cash.turbine.test
import com.github.odaridavid.opencharge.api.Address
import com.github.odaridavid.opencharge.api.Poi
import com.github.odaridavid.opencharge.api.PoiRepository
import com.github.odaridavid.opencharge.presentation.MapScreenIntent
import com.github.odaridavid.opencharge.presentation.MapViewModel
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MapViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @MockK
    val mockPoiRepository = mockk<PoiRepository>(relaxed = true)

    @Test
    fun `when an intent to load poi data is sent, the result is displayed`() = runBlocking {
        val result = listOf(
            Poi(
                title = "Düsseldorf",
                noOfChargingPoints = 4,
                address = Address(
                    addressLineOne = "Semstrasse 12",
                    addressLineTwo = "Flingern Nord",
                    latitude = 12.00,
                    longitude = 12.00,
                    postCode = "40899"
                )
            )
        )
        coEvery { mockPoiRepository.retrievePois() } returns flowOf(
            Result.success<List<Poi>>(result)
        )

        val viewModel = createViewModel()

        viewModel.processIntent(MapScreenIntent.LoadPoiData)

        viewModel.state.test {
            awaitItem().also { state ->
                assert(!state.isLoading)
                assert(state.error == null)
                assert(state.pois.isNotEmpty())
                assert(state.pois == result)
            }
        }
    }

    @Test
    fun `when an intent to fetch weather data is sent and it fails, the error is displayed`() =
        runBlocking {
            val exception = Throwable("An issue occured")
            coEvery { mockPoiRepository.retrievePois() } returns flowOf(
                Result.failure<List<Poi>>(exception)
            )

            val viewModel = createViewModel()

            viewModel.processIntent(MapScreenIntent.LoadPoiData)

            viewModel.state.test {
                awaitItem().also { state ->
                    assert(!state.isLoading)
                    assert(state.error == exception)
                }
            }
        }

    @Test
    fun `when an intent to stop poi refresh is sent , then the correct methods are called`() = runBlocking {
        coEvery { mockPoiRepository.cancelRepeatingTransactions() } returns Unit
        
        val viewModel = createViewModel()

        viewModel.processIntent(MapScreenIntent.StopPoiDataRefresh)

        verify { mockPoiRepository.cancelRepeatingTransactions() }
    }

    // region Helper Method

    private fun createViewModel() = MapViewModel(poiRepository = mockPoiRepository)

    // endregion
}
