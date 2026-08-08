package com.connect.payroll.feature.data.di

import com.connect.payroll.feature.data.payroll.PayrollRepositoryImpl
import com.connect.payroll.feature.domain.PayrollRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsPayrollRepository(impl: PayrollRepositoryImpl): PayrollRepository
}
