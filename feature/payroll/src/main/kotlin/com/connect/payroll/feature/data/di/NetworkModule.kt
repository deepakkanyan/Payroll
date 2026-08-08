package com.connect.payroll.feature.data.di

import com.connect.payroll.feature.data.payroll.remote.FakeRemoteDataSource
import com.connect.payroll.feature.data.payroll.remote.PayrollRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindsPayrollRemoteDataSource(impl: FakeRemoteDataSource): PayrollRemoteDataSource
}
