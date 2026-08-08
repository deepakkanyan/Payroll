package com.connect.payroll.feature.presentation.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.connect.payroll.core.designsystem.component.MoneyText
import com.connect.payroll.core.designsystem.text.asString
import com.connect.payroll.feature.domain.model.NewEmployee
import com.connect.payroll.feature.R
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePayrollScreen(
    onSaved: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreatePayrollViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.saved.collectLatest { onSaved() }
    }

    val employees = when (val state = uiState) {
        is CreatePayrollUiState.Success -> state.employees
        else -> emptyList()
    }
    val inlineMessage = uiState.message
    val isSaving = uiState is CreatePayrollUiState.Loading

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.create_payroll_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.create_payroll_back_content_description),
                        )
                    }
                },
            )
        },
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            EmployeeInputRow(onAdd = viewModel::addEmployee)

            if (inlineMessage != null) {
                Text(
                    text = inlineMessage.asString(),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                )
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f, fill = true)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(employees, key = { index, _ -> index }) { _, employee ->
                    DraftEmployeeRow(employee = employee, onRemove = { viewModel.removeEmployee(employee) })
                }
            }

            Button(
                onClick = viewModel::save,
                enabled = employees.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp))
                }
                Text(stringResource(R.string.create_payroll_save))
            }
        }
    }
}

@Composable
private fun EmployeeInputRow(onAdd: (name: String, wagesText: String, isExempt: Boolean) -> Unit) {
    var name by rememberSaveable { mutableStateOf("") }
    var wagesText by rememberSaveable { mutableStateOf("") }
    var isExempt by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(R.string.create_payroll_employee_name_label)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = wagesText,
            onValueChange = { wagesText = it },
            label = { Text(stringResource(R.string.create_payroll_wages_label)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(stringResource(R.string.create_payroll_exempt_label), modifier = Modifier.weight(1f))
            Switch(checked = isExempt, onCheckedChange = { isExempt = it })
        }
        Button(
            onClick = {
                onAdd(name, wagesText, isExempt)
                name = ""
                wagesText = ""
                isExempt = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        ) {
            Text(stringResource(R.string.create_payroll_add_employee))
        }
    }
}

@Composable
private fun DraftEmployeeRow(employee: NewEmployee, onRemove: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(employee.name, style = MaterialTheme.typography.bodyLarge)
                MoneyText(money = employee.wages, style = MaterialTheme.typography.bodyLarge)
                if (employee.isExempt) {
                    Text(stringResource(R.string.create_payroll_exempt_label), style = MaterialTheme.typography.labelSmall)
                }
            }
            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = stringResource(
                        R.string.create_payroll_remove_employee_content_description,
                        employee.name,
                    ),
                )
            }
        }
    }
}
