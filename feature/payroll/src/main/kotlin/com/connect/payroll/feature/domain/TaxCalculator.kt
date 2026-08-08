package com.connect.payroll.feature.domain

import com.connect.payroll.feature.domain.model.PayrollLineItem
import com.connect.payroll.feature.domain.model.Employee
import com.connect.payroll.core.model.Money
import java.math.BigDecimal

/**
 * Pure tax rule, zero Android imports so it's trivially unit-testable:
 *
 * If wages > 1000 AND the employee is not exempt, tax is 5% of the FULL wages
 * (not just the amount above 1000). Otherwise tax is 0. Net = wages - tax.
 */
object TaxCalculator {

    private val TAXABLE_WAGES_THRESHOLD: Money = Money.of(1_000L)
    private val TAX_RATE: BigDecimal = BigDecimal("0.05")

    fun calculate(employee: Employee): PayrollLineItem {
        val isTaxable = employee.wages > TAXABLE_WAGES_THRESHOLD && !employee.isExempt
        val tax = if (isTaxable) employee.wages.times(TAX_RATE) else Money.ZERO
        return PayrollLineItem(
            employee = employee,
            tax = tax,
            net = employee.wages - tax,
        )
    }
}
