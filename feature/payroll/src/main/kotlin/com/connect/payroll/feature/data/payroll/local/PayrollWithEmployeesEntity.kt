package com.connect.payroll.feature.data.payroll.local

import androidx.room.Embedded
import androidx.room.Relation

data class PayrollWithEmployeesEntity(
    @Embedded val payroll: PayrollEntity,
    @Relation(parentColumn = "id", entityColumn = "payrollId")
    val employees: List<EmployeeEntity>,
)

/** One list row, aggregated entirely in SQL — no in-memory scan over every employee. */
data class PayrollSummaryRow(
    val id: Long,
    val createdAtEpochMillis: Long,
    val employeeCount: Int,
    val totalWagesCents: Long,
)
