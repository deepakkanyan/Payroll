package com.connect.payroll.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

/**
 * The one shared HTTP client for the whole app. A feature that talks to a real backend builds its
 * own Retrofit *service interface* and injects this [Retrofit] instance to create it (via
 * `retrofit.create(MyApiService::class.java)`) — nothing feature-specific belongs in this module.
 *
 * [BASE_URL] is a placeholder: no feature calls a real backend today — payroll's remote data is
 * fully mocked via `FakeRemoteDataSource`. Point this at a real API once one exists; every feature
 * built against the injected [Retrofit] picks that up automatically.
 */
@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    internal const val BASE_URL = "https://api.payroll.example/"

    @Provides
    @Singleton
    fun providesJson(): Json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
}
