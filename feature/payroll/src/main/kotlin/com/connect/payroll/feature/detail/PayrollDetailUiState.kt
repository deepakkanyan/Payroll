package com.connect.payroll.feature.detail

import com.connect.payroll.core.designsystem.text.UiText
import com.connect.payroll.feature.domain.model.PayrollDetail

sealed interface PayrollDetailUiState {
    data object Loading : PayrollDetailUiState

    /** The payroll doesn't exist (bad id, or it was removed). */
    data object Empty : PayrollDetailUiState
    data class Success(val detail: PayrollDetail) : PayrollDetailUiState
    data class Error(val message: UiText) : PayrollDetailUiState
}
