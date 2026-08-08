package com.connect.payroll.feature.domain.model

import com.connect.payroll.core.model.Money

data class Employee(
    val id: Long,
    val payrollId: Long,
    val name: String,
    val wages: Money,
    val isExempt: Boolean,
)
