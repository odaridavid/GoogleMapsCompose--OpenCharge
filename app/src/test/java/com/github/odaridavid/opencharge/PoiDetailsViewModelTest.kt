package com.github.odaridavid.opencharge

import app.cash.turbine.test
import com.github.odaridavid.opencharge.api.Address
import com.github.odaridavid.opencharge.api.Poi
import com.github.odaridavid.opencharge.api.PoiRepository
import com.github.odaridavid.opencharge.presentation.PoiDetailsIntent
import com.github.odaridavid.opencharge.presentation.PoiDetailsViewModel
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PoiDetailsViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @MockK
    val mockPoiRepository = mockk<PoiRepository>(relaxed = true)

    @Test
    fun `when an intent to load poi data is sent, the result is displayed`() = runBlocking {
        coEvery { mockPoiRepository.retrievePois() } returns flowOf(
            Result.success<List<Poi>>(
                listOf<Poi>(
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
                    ),
                    Poi(
                        title = "Köln",
                        noOfChargingPoints = 4,
                        address = Address(
                            addressLineOne = "Semstrasse 12",
                            addressLineTwo = "Flingern Nord",
                            latitude = 12.00,
                            longitude = 12.00,
                            postCode = "40899"
                        )
                    ),
                )
            )
        )

        val viewModel = createViewModel()

        viewModel.processIntent(PoiDetailsIntent.LoadPoiData("Köln"))

        val expectedPoi = Poi(
            title = "Köln",
            noOfChargingPoints = 4,
            address = Address(
                addressLineOne = "Semstrasse 12",
                addressLineTwo = "Flingern Nord",
                latitude = 12.00,
                longitude = 12.00,
                postCode = "40899"
            )
        )

        viewModel.state.test {
            awaitItem().also { state ->
                assert(!state.isLoading)
                assert(state.error == null)
                assert(state.poi == expectedPoi)
            }
        }
    }

    @Test
    fun `when an intent to load poi data is sent and it fails, the error is displayed`() =
        runBlocking {
            val exception = Throwable("An issue occured")
            coEvery { mockPoiRepository.retrievePois() } returns flowOf(
                Result.failure<List<Poi>>(exception)
            )

            val viewModel = createViewModel()

            viewModel.processIntent(PoiDetailsIntent.LoadPoiData("title"))

            viewModel.state.test {
                awaitItem().also { state ->
                    assert(!state.isLoading)
                    assert(state.error == exception)
                }
            }
        }

    @Test
    fun `when vm is initialised , then loading state is shown`() = runBlocking {
        val viewModel = createViewModel()

        viewModel.state.test {
            awaitItem().also { state ->
                assert(state.isLoading)
                assert(state.error == null)
            }
        }
    }

    // region Helper Method

    private fun createViewModel() = PoiDetailsViewModel(poiRepository = mockPoiRepository)

    // endregion
}
