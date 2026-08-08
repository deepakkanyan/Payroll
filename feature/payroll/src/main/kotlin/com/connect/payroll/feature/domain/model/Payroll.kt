package com.connect.payroll.feature.domain.model

import com.connect.payroll.core.model.Money
import java.time.Instant

data class Payroll(
    val id: Long,
    val createdAt: Instant,
)

/**
 * A single row for the payroll list screen. [employeeCount] and [totalWages] are computed
 * by an aggregate database query, not by loading every employee into memory.
 */
data class PayrollListItem(
    val payroll: Payroll,
    val employeeCount: Int,
    val totalWages: Money,
)

/** The raw contents of one payroll and its employees, as persisted — no tax math applied. */
data class PayrollWithEmployees(
    val payroll: Payroll,
    val employees: List<Employee>,
)
