package com.connect.payroll.feature.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connect.payroll.core.designsystem.text.UiText
import com.connect.payroll.feature.domain.usecase.GetPayrollListUseCase
import com.connect.payroll.feature.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class PayrollListViewModel @Inject constructor(
    getPayrollList: GetPayrollListUseCase,
) : ViewModel() {

    val uiState: StateFlow<PayrollListUiState> = getPayrollList()
        .map { payrolls ->
            if (payrolls.isEmpty()) PayrollListUiState.Empty else PayrollListUiState.Success(payrolls)
        }
        .catch { throwable ->
            val message = throwable.message?.let(UiText::Dynamic)
                ?: UiText.Resource(R.string.payroll_list_load_error)
            emit(PayrollListUiState.Error(message))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = PayrollListUiState.Loading,
        )

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
