package com.github.odaridavid.opencharge.di

import com.github.odaridavid.opencharge.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

object HeadersInterceptor:Interceptor {

    private const val HEADER_API_KEY = "X-API-Key"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newRequest = request.newBuilder()
            .header(HEADER_API_KEY, BuildConfig.OPEN_CHARGE_API)
            .build()

        return chain.proceed(newRequest)
    }
}
