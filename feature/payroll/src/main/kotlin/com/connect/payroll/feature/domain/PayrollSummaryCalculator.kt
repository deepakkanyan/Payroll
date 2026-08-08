package com.connect.payroll.feature.domain

import com.connect.payroll.feature.domain.model.PayrollLineItem
import com.connect.payroll.feature.domain.model.PayrollSummary
import com.connect.payroll.core.model.sum

/** Pure aggregation of already-taxed line items, zero Android imports. */
object PayrollSummaryCalculator {

    fun calculate(lineItems: List<PayrollLineItem>): PayrollSummary = PayrollSummary(
        totalWages = lineItems.map { it.employee.wages }.sum(),
        totalTaxes = lineItems.map { it.tax }.sum(),
        totalNet = lineItems.map { it.net }.sum(),
    )
}
