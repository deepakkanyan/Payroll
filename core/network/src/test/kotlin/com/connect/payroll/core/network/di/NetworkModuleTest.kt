package com.connect.payroll.core.network.di

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class NetworkModuleTest {

    @Test
    fun `retrofit is built against the shared base URL`() {
        val json = NetworkModule.providesJson()
        val client = NetworkModule.providesOkHttpClient()
        val retrofit = NetworkModule.providesRetrofit(client, json)

        assertEquals(NetworkModule.BASE_URL, retrofit.baseUrl().toString())
    }

    @Test
    fun `okHttpClient has the shared logging interceptor configured`() {
        val client = NetworkModule.providesOkHttpClient()

        assertTrue(client.interceptors.any { it is okhttp3.logging.HttpLoggingInterceptor })
    }

    @Test
    fun `json ignores unknown keys so a growing API response doesn't break parsing`() {
        val json = NetworkModule.providesJson()

        assertTrue(json.configuration.ignoreUnknownKeys)
    }
}
