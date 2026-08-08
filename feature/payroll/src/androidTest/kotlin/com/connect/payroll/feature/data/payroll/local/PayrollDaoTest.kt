package com.connect.payroll.feature.data.payroll.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Scoped to just this feature's own entities — deliberately not [com.connect.payroll.database.AppDatabase],
 * so this test stays self-contained inside :feature:payroll instead of depending on :app.
 */
@Database(entities = [PayrollEntity::class, EmployeeEntity::class], version = 1)
internal abstract class PayrollTestDatabase : RoomDatabase() {
    abstract fun payrollDao(): PayrollDao
}

@RunWith(AndroidJUnit4::class)
class PayrollDaoTest {

    private lateinit var database: PayrollTestDatabase
    private lateinit var dao: PayrollDao

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            PayrollTestDatabase::class.java,
        ).allowMainThreadQueries().build()
        dao = database.payrollDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun payrollWithEmployeesRelationReturnsEveryEmployeeForThatPayroll() = runTest {
        val payrollId = dao.insertPayroll(PayrollEntity(createdAtEpochMillis = 1_000L))
        dao.insertEmployees(
            listOf(
                EmployeeEntity(
                    payrollId = payrollId,
                    name = "Sarah Mitchell",
                    wagesCents = 90_000,
                    isExempt = false,
                ),
                EmployeeEntity(
                    payrollId = payrollId,
                    name = "James Caldwell",
                    wagesCents = 190_000,
                    isExempt = true,
                ),
                EmployeeEntity(
                    payrollId = payrollId,
                    name = "Laura Nguyen",
                    wagesCents = 200_000,
                    isExempt = false,
                ),
            ),
        )

        val result = dao.observePayrollWithEmployees(payrollId).first()

        assertNotNull(result)
        assertEquals(payrollId, result!!.payroll.id)
        assertEquals(3, result.employees.size)
        assertEquals(
            setOf("Sarah Mitchell", "James Caldwell", "Laura Nguyen"),
            result.employees.map { it.name }.toSet(),
        )
    }

    @Test
    fun payrollWithEmployeesRelationIsNullForAnUnknownId() = runTest {
        val result = dao.observePayrollWithEmployees(payrollId = 999L).first()

        assertEquals(null, result)
    }

    @Test
    fun employeesFromADifferentPayrollAreNotIncludedInTheRelation() = runTest {
        val firstPayrollId = dao.insertPayroll(PayrollEntity(createdAtEpochMillis = 1_000L))
        val secondPayrollId = dao.insertPayroll(PayrollEntity(createdAtEpochMillis = 2_000L))
        dao.insertEmployees(
            listOf(
                EmployeeEntity(
                    payrollId = firstPayrollId,
                    name = "In First Payroll",
                    wagesCents = 100_000,
                    isExempt = false,
                ),
                EmployeeEntity(
                    payrollId = secondPayrollId,
                    name = "In Second Payroll",
                    wagesCents = 100_000,
                    isExempt = false,
                ),
            ),
        )

        val result = dao.observePayrollWithEmployees(firstPayrollId).first()

        assertEquals(1, result!!.employees.size)
        assertEquals("In First Payroll", result.employees.single().name)
    }

    @Test
    fun summaryQueryAggregatesEmployeeCountAndTotalWagesInSql() = runTest {
        val payrollId = dao.insertPayroll(PayrollEntity(createdAtEpochMillis = 1_000L))
        dao.insertEmployees(
            listOf(
                EmployeeEntity(
                    payrollId = payrollId,
                    name = "Sarah Mitchell",
                    wagesCents = 90_000,
                    isExempt = false,
                ),
                EmployeeEntity(
                    payrollId = payrollId,
                    name = "James Caldwell",
                    wagesCents = 190_000,
                    isExempt = true,
                ),
                EmployeeEntity(
                    payrollId = payrollId,
                    name = "Laura Nguyen",
                    wagesCents = 200_000,
                    isExempt = false,
                ),
            ),
        )

        val summaries = dao.observePayrollSummaries().first()

        assertEquals(1, summaries.size)
        assertEquals(3, summaries.single().employeeCount)
        assertEquals(480_000L, summaries.single().totalWagesCents)
    }
}
