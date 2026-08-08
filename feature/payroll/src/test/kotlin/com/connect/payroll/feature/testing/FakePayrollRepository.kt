package com.connect.payroll.feature.testing

import com.connect.payroll.feature.domain.PayrollRepository
import com.connect.payroll.feature.domain.model.Employee
import com.connect.payroll.feature.domain.model.NewEmployee
import com.connect.payroll.feature.domain.model.Payroll
import com.connect.payroll.feature.domain.model.PayrollListItem
import com.connect.payroll.feature.domain.model.PayrollWithEmployees
import com.connect.payroll.core.model.sum
import java.time.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/** In-memory [PayrollRepository] for ViewModel tests — no Room, no Hilt, no fake network. */
class FakePayrollRepository : PayrollRepository {

    private val payrolls = MutableStateFlow<List<PayrollWithEmployees>>(emptyList())

    fun setPayrolls(value: List<PayrollWithEmployees>) {
        payrolls.value = value
    }

    override fun observePayrollListItems(): Flow<List<PayrollListItem>> =
        payrolls.map { list -> list.map { it.toListItem() } }

    override fun observePayrollWithEmployees(payrollId: Long): Flow<PayrollWithEmployees?> =
        payrolls.map { list -> list.find { it.payroll.id == payrollId } }

    override suspend fun createPayroll(employees: List<NewEmployee>): Long {
        val newId = (payrolls.value.maxOfOrNull { it.payroll.id } ?: 0L) + 1
        val payroll = Payroll(id = newId, createdAt = Instant.now())
        val persistedEmployees = employees.mapIndexed { index, draft ->
            Employee(
                id = index.toLong(),
                payrollId = newId,
                name = draft.name,
                wages = draft.wages,
                isExempt = draft.isExempt,
            )
        }
        payrolls.update { it + PayrollWithEmployees(payroll, persistedEmployees) }
        return newId
    }

    override suspend fun seedIfEmpty() {
        // no-op: tests populate data explicitly via setPayrolls
    }

    private fun PayrollWithEmployees.toListItem() = PayrollListItem(
        payroll = payroll,
        employeeCount = employees.size,
        totalWages = employees.map { it.wages }.sum(),
    )
}
