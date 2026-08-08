package com.connect.payroll.feature.create

import com.connect.payroll.core.designsystem.text.UiText
import com.connect.payroll.feature.domain.model.NewEmployee

/**
 * Loading = save in flight. Empty/Success both keep the form fully interactive and differ only
 * by whether an employee has been drafted yet; [message] carries an inline validation hint for
 * either. Error is reserved for an unexpected failure while saving.
 */
sealed interface CreatePayrollUiState {
    val message: UiText?

    data object Loading : CreatePayrollUiState {
        override val message: UiText? = null
    }
    data class Empty(override val message: UiText? = null) : CreatePayrollUiState
    data class Success(val employees: List<NewEmployee>, override val message: UiText? = null) : CreatePayrollUiState
    data class Error(override val message: UiText) : CreatePayrollUiState
}
