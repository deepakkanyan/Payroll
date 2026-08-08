package com.connect.payroll.core.model

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * A monetary amount, always normalized to 2 decimal places with [RoundingMode.HALF_UP].
 *
 * Backed by [BigDecimal] rather than [Double] so that payroll math never suffers from
 * floating-point representation error. All arithmetic re-normalizes scale, so two [Money]
 * values are only ever equal when their underlying amounts are equal at cent precision.
 */
@JvmInline
value class Money private constructor(val amount: BigDecimal) : Comparable<Money> {

    operator fun plus(other: Money): Money = of(amount + other.amount)

    operator fun minus(other: Money): Money = of(amount - other.amount)

    /** Multiplies by an arbitrary-precision rate (e.g. a tax rate), rounding the result to cents. */
    fun times(rate: BigDecimal): Money = of(amount.multiply(rate))

    override fun compareTo(other: Money): Int = amount.compareTo(other.amount)

    override fun toString(): String = amount.toPlainString()

    companion object {
        val ZERO: Money = Money(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))

        fun of(value: BigDecimal): Money = Money(value.setScale(2, RoundingMode.HALF_UP))

        fun of(value: Long): Money = of(BigDecimal(value))

        fun of(value: String): Money = of(BigDecimal(value))
    }
}

fun Iterable<Money>.sum(): Money = fold(Money.ZERO) { acc, money -> acc + money }
