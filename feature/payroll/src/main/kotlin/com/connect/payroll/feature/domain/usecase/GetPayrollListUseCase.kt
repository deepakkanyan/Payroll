package com.connect.payroll.feature.domain.usecase

import com.connect.payroll.feature.domain.PayrollRepository
import com.connect.payroll.feature.domain.model.PayrollListItem
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetPayrollListUseCase @Inject constructor(
    private val repository: PayrollRepository,
) {
    operator fun invoke(): Flow<List<PayrollListItem>> = repository.observePayrollListItems()
}
