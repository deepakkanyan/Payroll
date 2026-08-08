package com.connect.payroll.feature.data.payroll.mapper

import com.connect.payroll.feature.data.mapper.centsToMoney
import com.connect.payroll.feature.data.mapper.toCents
import com.connect.payroll.feature.data.payroll.local.EmployeeEntity
import com.connect.payroll.feature.data.payroll.local.PayrollEntity
import com.connect.payroll.feature.data.payroll.local.PayrollSummaryRow
import com.connect.payroll.feature.data.payroll.local.PayrollWithEmployeesEntity
import com.connect.payroll.feature.domain.model.Employee
import com.connect.payroll.feature.domain.model.NewEmployee
import com.connect.payroll.feature.domain.model.Payroll
import com.connect.payroll.feature.domain.model.PayrollListItem
import com.connect.payroll.feature.domain.model.PayrollWithEmployees
import java.time.Instant

internal fun PayrollEntity.toModel(): Payroll = Payroll(
    id = id,
    createdAt = Instant.ofEpochMilli(createdAtEpochMillis),
)

internal fun EmployeeEntity.toModel(): Employee = Employee(
    id = id,
    payrollId = payrollId,
    name = name,
    wages = wagesCents.centsToMoney(),
    isExempt = isExempt,
)

internal fun PayrollWithEmployeesEntity.toModel(): PayrollWithEmployees = PayrollWithEmployees(
    payroll = payroll.toModel(),
    employees = employees.map { it.toModel() },
)

internal fun PayrollSummaryRow.toModel(): PayrollListItem = PayrollListItem(
    payroll = Payroll(id = id, createdAt = Instant.ofEpochMilli(createdAtEpochMillis)),
    employeeCount = employeeCount,
    totalWages = totalWagesCents.centsToMoney(),
)

internal fun NewEmployee.toEntity(payrollId: Long): EmployeeEntity = EmployeeEntity(
    payrollId = payrollId,
    name = name,
    wagesCents = wages.toCents(),
    isExempt = isExempt,
)
