package com.connect.payroll.feature.presentation.list

import com.connect.payroll.core.designsystem.text.UiText
import com.connect.payroll.feature.domain.model.PayrollListItem

sealed interface PayrollListUiState {
    data object Loading : PayrollListUiState
    data object Empty : PayrollListUiState
    data class Success(val payrolls: List<PayrollListItem>) : PayrollListUiState
    data class Error(val message: UiText) : PayrollListUiState
}
