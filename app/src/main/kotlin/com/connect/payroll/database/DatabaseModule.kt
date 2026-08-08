package com.connect.payroll.database

import android.content.Context
import androidx.room.Room
import com.connect.payroll.feature.data.payroll.local.PayrollDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "app.db",
        ).build()

    @Provides
    fun providesPayrollDao(database: AppDatabase): PayrollDao = database.payrollDao()
}
