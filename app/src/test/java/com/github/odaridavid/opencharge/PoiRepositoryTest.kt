package com.github.odaridavid.opencharge

import app.cash.turbine.test
import com.github.odaridavid.opencharge.api.Address
import com.github.odaridavid.opencharge.api.Poi
import com.github.odaridavid.opencharge.impl.DefaultPoiRepository
import com.github.odaridavid.opencharge.impl.data.AddressInfo
import com.github.odaridavid.opencharge.impl.data.OpenChargeService
import com.github.odaridavid.opencharge.impl.data.PoiResponse
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response

class PoiRepositoryTest {

    @MockK
    val mockOpenChargeService = mockk<OpenChargeService>(relaxed = true)

    @Test
    fun `when we load weather data with unsuccessful response, then an error is emmitted`() =
        runBlocking {
            coEvery {
                mockOpenChargeService.getPoiList(
                    longitude = any(),
                    latitude = any(),
                    distance = any()
                )
            } returns Response.success<List<PoiResponse>>(null)

            val poiRepository = createPoiRepository()

            poiRepository.retrievePois().test {
                awaitItem().also { result ->
                    Truth.assertThat(result.exceptionOrNull()?.message)
                        .contains("Unexpected Error Occurred")
                }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `when we load poi data successfuly, then a successfully mapped result is emitted`() =
        runBlocking {
            coEvery {
                mockOpenChargeService.getPoiList(
                    longitude = any(),
                    latitude = any(),
                    distance = any()
                )
            } returns Response.success<List<PoiResponse>>(
                listOf(
                    PoiResponse(
                        addressInfo = AddressInfo(
                            title = "Düsseldorf",
                            latitude = 12.00,
                            longitude = 12.00,
                            addressLineOne = "Semstrasse 12",
                            addressLineTwo = "Flingern Nord",
                            town = "Düsseldorf",
                            postcode = "40899"
                        ),
                        numberOfPoints = 4
                    )
                )
            )
            val poiRepository = createPoiRepository()

            val expectedResult = listOf(
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

            poiRepository.retrievePois().test {
                awaitItem().also { result ->
                    assert(result.getOrThrow() == expectedResult)
                }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `when we load poi data and an error occurs, then a failure is emitted`() =
        runBlocking {
            coEvery {
                mockOpenChargeService.getPoiList(
                    longitude = any(),
                    latitude = any(),
                    distance = any()
                )
            } returns Response.error<List<PoiResponse>>(
                404,
                "{}".toResponseBody()
            )

            val poiRepository = createPoiRepository()

            poiRepository.retrievePois().test {
                awaitItem().also { result ->
                    assert(result.exceptionOrNull() is Throwable)
                }
                awaitComplete()
            }
        }

    // region Helper Method

    private fun createPoiRepository() = DefaultPoiRepository(
        openChargeService = mockOpenChargeService
    )

    // endregion
}
