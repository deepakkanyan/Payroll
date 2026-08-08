package com.connect.payroll.feature.data.mapper

import com.connect.payroll.core.model.Money
import java.math.BigDecimal
import java.math.RoundingMode

/** Room has no BigDecimal column type, so money is persisted as integer minor units (cents). */
internal fun Money.toCents(): Long = amount.movePointRight(2).setScale(0, RoundingMode.HALF_UP).toLong()

internal fun Long.centsToMoney(): Money = Money.of(BigDecimal.valueOf(this, 2))
