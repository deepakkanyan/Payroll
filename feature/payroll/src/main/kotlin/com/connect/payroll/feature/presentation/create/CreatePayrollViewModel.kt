package com.connect.payroll.feature.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connect.payroll.core.designsystem.text.UiText
import com.connect.payroll.feature.domain.model.CreatePayrollResult
import com.connect.payroll.feature.domain.usecase.CreatePayrollUseCase
import com.connect.payroll.core.model.Money
import com.connect.payroll.feature.domain.model.NewEmployee
import com.connect.payroll.feature.R
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CreatePayrollViewModel @Inject constructor(
    private val createPayroll: CreatePayrollUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<CreatePayrollUiState>(CreatePayrollUiState.Empty())
    val uiState: StateFlow<CreatePayrollUiState> = _uiState.asStateFlow()

    private val _saved = MutableSharedFlow<Unit>()
    val saved: SharedFlow<Unit> = _saved.asSharedFlow()

    fun addEmployee(name: String, wagesText: String, isExempt: Boolean) {
        val trimmedName = name.trim()
        val wages = wagesText.trim().toBigDecimalOrNull()
        val validationError = when {
            trimmedName.isBlank() -> UiText.Resource(R.string.create_payroll_name_required)
            wages == null || wages <= BigDecimal.ZERO -> UiText.Resource(R.string.create_payroll_wages_invalid)
            else -> null
        }
        if (validationError != null) {
            _uiState.update { it.withMessage(validationError) }
            return
        }
        val newEmployee = NewEmployee(trimmedName, Money.of(wages!!), isExempt)
        _uiState.update { current -> CreatePayrollUiState.Success(current.employeesOrEmpty + newEmployee) }
    }

    fun removeEmployee(employee: NewEmployee) {
        _uiState.update { current ->
            val remaining = current.employeesOrEmpty - employee
            if (remaining.isEmpty()) {
                CreatePayrollUiState.Empty(current.message)
            } else {
                CreatePayrollUiState.Success(remaining, current.message)
            }
        }
    }

    fun save() {
        val current = _uiState.value
        if (current !is CreatePayrollUiState.Success) return
        viewModelScope.launch {
            _uiState.value = CreatePayrollUiState.Loading
            try {
                when (val result = createPayroll(current.employees)) {
                    is CreatePayrollResult.Success -> _saved.emit(Unit)
                    is CreatePayrollResult.ValidationError -> {
                        _uiState.value = current.copy(message = UiText.Dynamic(result.message))
                    }
                }
            } catch (throwable: Throwable) {
                val message = throwable.message?.let(UiText::Dynamic)
                    ?: UiText.Resource(R.string.create_payroll_save_error)
                _uiState.value = CreatePayrollUiState.Error(message)
            }
        }
    }

    private val CreatePayrollUiState.employeesOrEmpty: List<NewEmployee>
        get() = (this as? CreatePayrollUiState.Success)?.employees.orEmpty()

    private fun CreatePayrollUiState.withMessage(message: UiText?): CreatePayrollUiState = when (this) {
        is CreatePayrollUiState.Success -> copy(message = message)
        is CreatePayrollUiState.Empty -> copy(message = message)
        CreatePayrollUiState.Loading, is CreatePayrollUiState.Error -> this
    }
}
