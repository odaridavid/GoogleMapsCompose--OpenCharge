package com.github.odaridavid.opencharge.api

import kotlinx.coroutines.flow.Flow

interface PoiRepository {

    fun retrievePois(): Flow<Result<List<Poi>>>

    fun cancelRepeatingTransactions()

    fun resumeRepeatingTransactions()
}
