package com.connect.payroll.feature.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.connect.payroll.core.designsystem.text.UiText
import com.connect.payroll.feature.domain.usecase.GetPayrollDetailUseCase
import com.connect.payroll.feature.R
import com.connect.payroll.feature.presentation.navigation.PayrollDetailRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class PayrollDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getPayrollDetail: GetPayrollDetailUseCase,
) : ViewModel() {

    private val payrollId: Long = savedStateHandle.toRoute<PayrollDetailRoute>().payrollId

    val uiState: StateFlow<PayrollDetailUiState> = getPayrollDetail(payrollId)
        .map { detail ->
            if (detail == null) PayrollDetailUiState.Empty else PayrollDetailUiState.Success(detail)
        }
        .catch { throwable ->
            val message = throwable.message?.let(UiText::Dynamic)
                ?: UiText.Resource(R.string.payroll_detail_load_error)
            emit(PayrollDetailUiState.Error(message))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = PayrollDetailUiState.Loading,
        )
}

private const val STOP_TIMEOUT_MILLIS = 5_000L
