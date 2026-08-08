package com.connect.payroll.feature.domain

import com.connect.payroll.feature.domain.model.NewEmployee
import com.connect.payroll.feature.domain.model.PayrollListItem
import com.connect.payroll.feature.domain.model.PayrollWithEmployees
import kotlinx.coroutines.flow.Flow

/**
 * Single source of truth for payroll data. The implementation (in this feature's data/ package)
 * reads and writes Room so every [Flow] here reflects the on-device database — the app works
 * fully offline once seeded.
 */
interface PayrollRepository {

    /** All payrolls, newest first, with employee count and total wages pre-aggregated by the DB. */
    fun observePayrollListItems(): Flow<List<PayrollListItem>>

    /** A single payroll with its raw (untaxed) employees, or null if it doesn't exist. */
    fun observePayrollWithEmployees(payrollId: Long): Flow<PayrollWithEmployees?>

    /** Persists a new payroll with the given employees and returns its generated id. */
    suspend fun createPayroll(employees: List<NewEmployee>): Long

    /** Populates the database from the remote source on first launch, if it's currently empty. */
    suspend fun seedIfEmpty()
}
