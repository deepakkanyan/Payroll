package com.connect.payroll.feature.domain.usecase

import com.connect.payroll.feature.domain.PayrollRepository
import com.connect.payroll.feature.domain.PayrollSummaryCalculator
import com.connect.payroll.feature.domain.TaxCalculator
import com.connect.payroll.feature.domain.model.PayrollDetail
import com.connect.payroll.feature.domain.model.PayrollWithEmployees
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPayrollDetailUseCase @Inject constructor(
    private val repository: PayrollRepository,
) {
    operator fun invoke(payrollId: Long): Flow<PayrollDetail?> =
        repository.observePayrollWithEmployees(payrollId).map { it?.toDetail() }

    private fun PayrollWithEmployees.toDetail(): PayrollDetail {
        val lineItems = employees.map(TaxCalculator::calculate)
        return PayrollDetail(
            payroll = payroll,
            lineItems = lineItems,
            summary = PayrollSummaryCalculator.calculate(lineItems),
        )
    }
}
