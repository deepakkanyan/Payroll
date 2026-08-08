package com.connect.payroll.feature.data.payroll.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "employees",
    foreignKeys = [
        ForeignKey(
            entity = PayrollEntity::class,
            parentColumns = ["id"],
            childColumns = ["payrollId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("payrollId")],
)
data class EmployeeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val payrollId: Long,
    val name: String,
    val wagesCents: Long,
    val isExempt: Boolean,
)
