package com.connect.payroll.feature.domain.payroll

import com.connect.payroll.feature.domain.model.Employee
import com.connect.payroll.core.model.Money
import com.connect.payroll.feature.domain.PayrollSummaryCalculator
import com.connect.payroll.feature.domain.TaxCalculator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TaxCalculatorTest {

    private fun employee(name: String, wages: Long, isExempt: Boolean) = Employee(
        id = 0L,
        payrollId = 0L,
        name = name,
        wages = Money.of(wages),
        isExempt = isExempt,
    )

    @Test
    fun `wages at or below 1000 are never taxed`() {
        val result = TaxCalculator.calculate(employee("Sarah Mitchell", wages = 900, isExempt = false))

        assertEquals(Money.of(900), result.employee.wages)
        assertEquals(Money.ZERO, result.tax)
        assertEquals(Money.of(900), result.net)
    }

    @Test
    fun `exempt employees are never taxed regardless of wages`() {
        val result = TaxCalculator.calculate(employee("James Caldwell", wages = 1900, isExempt = true))

        assertEquals(Money.ZERO, result.tax)
        assertEquals(Money.of(1900), result.net)
    }

    @Test
    fun `wages over 1000 and not exempt are taxed 5 percent of the full wages`() {
        val result = TaxCalculator.calculate(employee("Laura Nguyen", wages = 2000, isExempt = false))

        assertEquals(Money.of(100), result.tax)
        assertEquals(Money.of(1900), result.net)
    }

    @Test
    fun `wages exactly at the 1000 threshold are not taxed`() {
        val result = TaxCalculator.calculate(employee("Threshold Employee", wages = 1000, isExempt = false))

        assertEquals(Money.ZERO, result.tax)
        assertEquals(Money.of(1000), result.net)
    }

    @Test
    fun `sample payroll produces the exact expected numbers`() {
        val employees = listOf(
            employee("Sarah Mitchell", wages = 900, isExempt = false),
            employee("James Caldwell", wages = 1900, isExempt = true),
            employee("Laura Nguyen", wages = 2000, isExempt = false),
        )

        val lineItems = employees.map(TaxCalculator::calculate)

        assertEquals(Money.ZERO, lineItems[0].tax)
        assertEquals(Money.of(900), lineItems[0].net)

        assertEquals(Money.ZERO, lineItems[1].tax)
        assertEquals(Money.of(1900), lineItems[1].net)

        assertEquals(Money.of(100), lineItems[2].tax)
        assertEquals(Money.of(1900), lineItems[2].net)

        val summary = PayrollSummaryCalculator.calculate(lineItems)
        assertEquals(Money.of(4800), summary.totalWages)
        assertEquals(Money.of(100), summary.totalTaxes)
        assertEquals(Money.of(4700), summary.totalNet)
    }
}
