package com.connect.payroll.feature.data.payroll.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface PayrollDao {

    @Insert
    suspend fun insertPayroll(payroll: PayrollEntity): Long

    @Insert
    suspend fun insertEmployees(employees: List<EmployeeEntity>)

    /**
     * One aggregated row per payroll — employee count and total wages are computed by SQL,
     * not by loading every employee into memory.
     */
    @Query(
        """
        SELECT p.id AS id,
               p.createdAtEpochMillis AS createdAtEpochMillis,
               COUNT(e.id) AS employeeCount,
               COALESCE(SUM(e.wagesCents), 0) AS totalWagesCents
        FROM payrolls p
        LEFT JOIN employees e ON e.payrollId = p.id
        GROUP BY p.id
        ORDER BY p.createdAtEpochMillis DESC
        """,
    )
    fun observePayrollSummaries(): Flow<List<PayrollSummaryRow>>

    @Transaction
    @Query("SELECT * FROM payrolls WHERE id = :payrollId")
    fun observePayrollWithEmployees(payrollId: Long): Flow<PayrollWithEmployeesEntity?>

    @Query("SELECT COUNT(*) FROM payrolls")
    suspend fun getPayrollCount(): Int
}
