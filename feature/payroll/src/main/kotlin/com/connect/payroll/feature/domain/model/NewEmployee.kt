package com.connect.payroll.feature.domain.model

import com.connect.payroll.core.model.Money

/** Employee input for a payroll that hasn't been persisted yet — no id, no payroll association. */
data class NewEmployee(
    val name: String,
    val wages: Money,
    val isExempt: Boolean,
)
