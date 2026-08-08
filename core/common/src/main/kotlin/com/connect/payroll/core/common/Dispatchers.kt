package com.connect.payroll.core.common

import javax.inject.Qualifier

enum class PayrollDispatcher {
    IO,
    Default,
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatcher: PayrollDispatcher)
