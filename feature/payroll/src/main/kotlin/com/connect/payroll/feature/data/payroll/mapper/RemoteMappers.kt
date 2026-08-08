package com.connect.payroll.feature.data.payroll.mapper

import com.connect.payroll.feature.data.mapper.toCents
import com.connect.payroll.feature.data.payroll.local.EmployeeEntity
import com.connect.payroll.feature.data.payroll.local.PayrollEntity
import com.connect.payroll.feature.data.payroll.remote.RemoteEmployee
import com.connect.payroll.feature.data.payroll.remote.RemotePayroll

internal fun RemotePayroll.toEntity(): PayrollEntity = PayrollEntity(
    createdAtEpochMillis = createdAt.toEpochMilli(),
)

internal fun RemoteEmployee.toEntity(payrollId: Long): EmployeeEntity = EmployeeEntity(
    payrollId = payrollId,
    name = name,
    wagesCents = wages.toCents(),
    isExempt = isExempt,
)
