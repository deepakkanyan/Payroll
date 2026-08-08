package com.connect.payroll.core.model

import java.math.BigDecimal
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MoneyTest {

    @Test
    fun `values with different scale but equal magnitude are equal`() {
        assertEquals(Money.of(BigDecimal("100")), Money.of(BigDecimal("100.00")))
    }

    @Test
    fun `construction rounds half up to two decimal places`() {
        assertEquals(Money.of("10.005"), Money.of("10.01"))
    }

    @Test
    fun `addition sums exactly`() {
        val result = Money.of("10.50") + Money.of("0.25")
        assertEquals(Money.of("10.75"), result)
    }

    @Test
    fun `times rounds half up to cents`() {
        val wages = Money.of(2000)
        val tax = wages.times(BigDecimal("0.05"))
        assertEquals(Money.of("100.00"), tax)
    }

    @Test
    fun `zero is the additive identity`() {
        val wages = Money.of("1234.56")
        assertEquals(wages, wages + Money.ZERO)
    }

    @Test
    fun `sum totals a list of money`() {
        val total = listOf(Money.of(900), Money.of(1900), Money.of(2000)).sum()
        assertEquals(Money.of(4800), total)
    }
}
