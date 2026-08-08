package com.connect.payroll

import android.app.Application
import com.connect.payroll.feature.domain.PayrollRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@HiltAndroidApp
class PayrollApplication : Application() {

    @Inject lateinit var repository: PayrollRepository

    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
          repository.seedIfEmpty()
        }
    }
}
