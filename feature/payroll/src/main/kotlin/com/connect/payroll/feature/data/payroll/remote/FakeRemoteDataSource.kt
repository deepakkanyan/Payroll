package com.connect.payroll.feature.data.payroll.remote

import com.connect.payroll.core.model.Money
import java.time.Instant
import javax.inject.Inject
import kotlinx.coroutines.delay

/**
 * Stands in for a real backend call. Returns a fixed sample payroll behind an artificial
 * delay so the rest of the app (loading states, offline-first Room caching) behaves exactly
 * as it would against a real network. Swap this for a Retrofit-backed implementation of
 * [PayrollRemoteDataSource] and nothing above this feature's data layer needs to change.
 */
internal class FakeRemoteDataSource @Inject constructor() : PayrollRemoteDataSource {

    override suspend fun fetchInitialPayrolls(): List<RemotePayroll> {
        delay(NETWORK_DELAY_MILLIS)

        return listOf(
            RemotePayroll(
                createdAt = Instant.parse("2026-07-01T09:00:00Z"),
                employees = listOf(
                    RemoteEmployee(name = "Sarah Mitchell", wages = Money.of(900), isExempt = false),
                    RemoteEmployee(name = "James Caldwell", wages = Money.of(1900), isExempt = true),
                    RemoteEmployee(name = "Laura Nguyen", wages = Money.of(2000), isExempt = false),
                ),
            ),
        )
    }

    private companion object {
        const val NETWORK_DELAY_MILLIS = 1_200L
    }
}
