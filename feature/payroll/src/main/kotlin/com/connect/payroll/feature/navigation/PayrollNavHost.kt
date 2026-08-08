package com.connect.payroll.feature.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.connect.payroll.feature.create.CreatePayrollScreen
import com.connect.payroll.feature.detail.PayrollDetailScreen
import com.connect.payroll.feature.list.PayrollListScreen

/**
 * Owns all cross-screen navigation. The list/create/detail packages in :feature never
 * reference each other directly — they only expose lambda callbacks that this graph wires up.
 */

fun NavGraphBuilder.payrollNavHost(navController: NavHostController) {
    navigation<PayrollGraph>(startDestination = PayrollListRoute) {

        composable<PayrollListRoute> {
            PayrollListScreen(
                onCreatePayroll = { navController.navigate(CreatePayrollRoute) },
                onPayrollClick = { payrollId -> navController.navigate(PayrollDetailRoute(payrollId)) },
            )
        }

        composable<CreatePayrollRoute> {
            CreatePayrollScreen(
                onSaved = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
            )
        }

        composable<PayrollDetailRoute> {
            PayrollDetailScreen(
                onBack = { navController.popBackStack() },
            )
        }
    }
}
