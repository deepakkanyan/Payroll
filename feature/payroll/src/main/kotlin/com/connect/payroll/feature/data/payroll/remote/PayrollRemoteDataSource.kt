package com.connect.payroll.feature.data.payroll.remote

import com.connect.payroll.core.model.Money
import java.time.Instant

data class RemoteEmployee(
    val name: String,
    val wages: Money,
    val isExempt: Boolean,
)

data class RemotePayroll(
    val createdAt: Instant,
    val employees: List<RemoteEmployee>,
)

/**
 * Boundary to a payroll backend. [FakeRemoteDataSource] is the only implementation today;
 * a real Retrofit-backed implementation (built on a shared client from :core:network) could be
 * swapped in behind this interface with no changes elsewhere in this feature.
 */
interface PayrollRemoteDataSource {
    suspend fun fetchInitialPayrolls(): List<RemotePayroll>
}
