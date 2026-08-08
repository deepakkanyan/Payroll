package com.connect.payroll.feature.data.payroll.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payrolls")
data class PayrollEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val createdAtEpochMillis: Long,
)
