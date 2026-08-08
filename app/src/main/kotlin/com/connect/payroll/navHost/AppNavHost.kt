package com.connect.payroll.navHost

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.connect.payroll.feature.navigation.PayrollGraph
import com.connect.payroll.feature.navigation.payrollNavHost

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = PayrollGraph){

        // each feature can have their own navigation map
        payrollNavHost(navController)
        // so on ...
    }


}