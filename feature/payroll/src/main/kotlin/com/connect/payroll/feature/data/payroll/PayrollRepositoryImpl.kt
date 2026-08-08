package com.connect.payroll.feature.data.payroll

import com.connect.payroll.core.common.Dispatcher
import com.connect.payroll.core.common.PayrollDispatcher
import com.connect.payroll.feature.data.payroll.mapper.toEntity
import com.connect.payroll.feature.data.payroll.mapper.toModel
import com.connect.payroll.feature.data.payroll.local.PayrollDao
import com.connect.payroll.feature.data.payroll.local.PayrollEntity
import com.connect.payroll.feature.domain.PayrollRepository
import com.connect.payroll.feature.domain.model.NewEmployee
import com.connect.payroll.feature.domain.model.PayrollListItem
import com.connect.payroll.feature.domain.model.PayrollWithEmployees
import com.connect.payroll.feature.data.payroll.remote.PayrollRemoteDataSource
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class PayrollRepositoryImpl @Inject constructor(
    private val dao: PayrollDao,
    private val remoteDataSource: PayrollRemoteDataSource,
    @param:Dispatcher(PayrollDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
) : PayrollRepository {

    override fun observePayrollListItems(): Flow<List<PayrollListItem>> =
        dao.observePayrollSummaries().map { rows -> rows.map { it.toModel() } }

    override fun observePayrollWithEmployees(payrollId: Long): Flow<PayrollWithEmployees?> =
        dao.observePayrollWithEmployees(payrollId).map { it?.toModel() }

    override suspend fun createPayroll(employees: List<NewEmployee>): Long = withContext(ioDispatcher) {
        val payrollId = dao.insertPayroll(PayrollEntity(createdAtEpochMillis = System.currentTimeMillis()))
        dao.insertEmployees(employees.map { it.toEntity(payrollId) })
        payrollId
    }

    override suspend fun seedIfEmpty() = withContext(ioDispatcher) {
        if (dao.getPayrollCount() > 0) return@withContext
        remoteDataSource.fetchInitialPayrolls().forEach { remotePayroll ->
            val payrollId = dao.insertPayroll(remotePayroll.toEntity())
            dao.insertEmployees(remotePayroll.employees.map { it.toEntity(payrollId) })
        }
    }
}
