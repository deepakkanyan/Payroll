package com.connect.payroll.feature.list

import app.cash.turbine.test
import com.connect.payroll.feature.domain.usecase.GetPayrollListUseCase
import com.connect.payroll.feature.domain.model.Employee
import com.connect.payroll.core.model.Money
import com.connect.payroll.feature.domain.model.Payroll
import com.connect.payroll.feature.domain.model.PayrollWithEmployees
import com.connect.payroll.feature.presentation.list.PayrollListUiState
import com.connect.payroll.feature.presentation.list.PayrollListViewModel
import com.connect.payroll.feature.testing.FakePayrollRepository
import com.connect.payroll.feature.testing.MainDispatcherExtension
import java.time.Instant
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
class PayrollListViewModelTest {

    @JvmField
    @RegisterExtension
    val mainDispatcherExtension = MainDispatcherExtension()

    private val repository = FakePayrollRepository()

    private fun viewModel() = PayrollListViewModel(GetPayrollListUseCase(repository))

    @Test
    fun `starts loading then emits empty when there are no payrolls`() = runTest {
        viewModel().uiState.test {
            assertEquals(PayrollListUiState.Loading, awaitItem())
            assertEquals(PayrollListUiState.Empty, awaitItem())
        }
    }

    @Test
    fun `emits success with the sample payroll's aggregated numbers`() = runTest {
        repository.setPayrolls(listOf(samplePayrollWithEmployees()))

        viewModel().uiState.test {
            assertEquals(PayrollListUiState.Loading, awaitItem())

            val success = awaitItem() as PayrollListUiState.Success
            val item = success.payrolls.single()
            assertEquals(3, item.employeeCount)
            assertEquals(Money.of(4800), item.totalWages)
        }
    }

    private fun samplePayrollWithEmployees(): PayrollWithEmployees = PayrollWithEmployees(
        payroll = Payroll(id = 1L, createdAt = Instant.parse("2026-07-01T09:00:00Z")),
        employees = listOf(
            Employee(id = 1, payrollId = 1, name = "Sarah Mitchell", wages = Money.of(900), isExempt = false),
            Employee(id = 2, payrollId = 1, name = "James Caldwell", wages = Money.of(1900), isExempt = true),
            Employee(id = 3, payrollId = 1, name = "Laura Nguyen", wages = Money.of(2000), isExempt = false),
        ),
    )
}
