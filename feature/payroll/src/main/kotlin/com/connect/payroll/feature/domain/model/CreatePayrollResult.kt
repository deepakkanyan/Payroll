package com.connect.payroll.feature.domain.model

sealed interface CreatePayrollResult {
    data class Success(val payrollId: Long) : CreatePayrollResult
    data class ValidationError(val message: String) : CreatePayrollResult
}
