package com.connect.payroll.feature.domain.usecase

import com.connect.payroll.feature.domain.PayrollRepository
import com.connect.payroll.feature.domain.model.CreatePayrollResult
import com.connect.payroll.core.model.Money
import com.connect.payroll.feature.domain.model.NewEmployee
import javax.inject.Inject

class CreatePayrollUseCase @Inject constructor(
    private val repository: PayrollRepository,
) {
    suspend operator fun invoke(employees: List<NewEmployee>): CreatePayrollResult {
        if (employees.isEmpty()) {
            return CreatePayrollResult.ValidationError("Add at least one employee.")
        }
        if (employees.any { it.name.isBlank() }) {
            return CreatePayrollResult.ValidationError("Every employee needs a name.")
        }
        if (employees.any { it.wages <= Money.ZERO }) {
            return CreatePayrollResult.ValidationError("Wages must be greater than zero.")
        }

        val payrollId = repository.createPayroll(employees)
        return CreatePayrollResult.Success(payrollId)
    }
}
