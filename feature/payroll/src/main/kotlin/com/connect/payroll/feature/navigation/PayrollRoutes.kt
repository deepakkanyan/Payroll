package com.connect.payroll.feature.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe navigation destinations for the 3 payroll screens. :app owns the NavHost that wires
 * these together; the list/create/detail packages never reference each other directly — only
 * these route types and the lambda callbacks each Screen exposes.
 */
@Serializable
data object PayrollGraph

@Serializable
data object PayrollListRoute

@Serializable
data object CreatePayrollRoute

@Serializable
data class PayrollDetailRoute(val payrollId: Long)
