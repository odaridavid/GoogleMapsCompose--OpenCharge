package com.github.odaridavid.opencharge.impl

import com.github.odaridavid.opencharge.api.Address
import com.github.odaridavid.opencharge.api.Poi
import com.github.odaridavid.opencharge.api.PoiRepository
import com.github.odaridavid.opencharge.impl.data.OpenChargeService
import com.github.odaridavid.opencharge.impl.data.PoiResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class DefaultPoiRepository @Inject constructor(
    private val openChargeService: OpenChargeService
) : PoiRepository {

    private var isRepeatingTransaction: AtomicBoolean = AtomicBoolean(false)

    override fun retrievePois(): Flow<Result<List<Poi>>> = flow {
        resumeRepeatingTransactions()
        fetchFromApi()
        while (isRepeatingTransaction.get()){
            fetchFromApi()
            delay(THIRTY_SECONDS)
        }
    }

    private suspend fun FlowCollector<Result<List<Poi>>>.fetchFromApi() {
        val response = openChargeService.getPoiList(
            longitude = DEFAULT_LONGITUDE,
            latitude = DEFAULT_LATITUDE,
            distance = DEFAULT_DISTANCE
        )
        if (response.isSuccessful && response.body() != null) {
            val poiData = response.body()?.map { it.toDomain() } ?: emptyList()
            emit(Result.success(poiData))
        } else {
            // TODO Log errors on a dashboard
            emit(Result.failure(Throwable("Unexpected Error Occurred : ${response.errorBody()}")))
            cancelRepeatingTransactions()
        }
    }

    override fun cancelRepeatingTransactions(){
        isRepeatingTransaction.compareAndSet(true, false)
    }

    override fun resumeRepeatingTransactions() {
        isRepeatingTransaction.compareAndSet(false, true)
    }

    companion object {
        private const val DEFAULT_LATITUDE = 52.526
        private const val DEFAULT_LONGITUDE = 13.415
        private const val DEFAULT_DISTANCE = 5
        private const val THIRTY_SECONDS = 30_000L
    }

}

private fun PoiResponse.toDomain(): Poi = Poi(
    title = addressInfo.title ?: "-",
    noOfChargingPoints = numberOfPoints ?: 0,
    address = Address(
        latitude = addressInfo.latitude,
        longitude = addressInfo.longitude,
        addressLineOne = addressInfo.addressLineOne ?: "-",
        addressLineTwo = addressInfo.addressLineTwo ?: "-",
        postCode = addressInfo.postcode ?: "-"
    )
)
