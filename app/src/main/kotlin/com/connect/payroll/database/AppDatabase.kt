package com.connect.payroll.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.connect.payroll.feature.data.payroll.local.EmployeeEntity
import com.connect.payroll.feature.data.payroll.local.PayrollDao
import com.connect.payroll.feature.data.payroll.local.PayrollEntity

/**
 * The app's single Room database. Room needs one compile-time list of every entity it manages,
 * so this class lives in :app — the only module allowed to see every feature — rather than in a
 * shared core module, which would otherwise need to depend on each feature to see its entities.
 * As more features add local persistence, their entities/DAOs get added here the same way
 * payroll's are below.
 */
@Database(
    entities = [PayrollEntity::class, EmployeeEntity::class],
    version = 1,
    exportSchema = true,
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun payrollDao(): PayrollDao
}
