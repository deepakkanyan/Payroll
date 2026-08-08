package com.connect.payroll.feature.domain.model

import com.connect.payroll.core.model.Money

/** One employee's taxed line for the detail screen. */
data class PayrollLineItem(
    val employee: Employee,
    val tax: Money,
    val net: Money,
)

data class PayrollSummary(
    val totalWages: Money,
    val totalTaxes: Money,
    val totalNet: Money,
)

data class PayrollDetail(
    val payroll: Payroll,
    val lineItems: List<PayrollLineItem>,
    val summary: PayrollSummary,
)
