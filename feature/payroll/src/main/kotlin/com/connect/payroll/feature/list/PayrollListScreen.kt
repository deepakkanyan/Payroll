package com.connect.payroll.feature.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.connect.payroll.core.designsystem.component.EmptyState
import com.connect.payroll.core.designsystem.component.ErrorState
import com.connect.payroll.core.designsystem.component.LoadingState
import com.connect.payroll.core.designsystem.component.MoneyText
import com.connect.payroll.core.designsystem.text.asString
import com.connect.payroll.feature.domain.model.PayrollListItem
import com.connect.payroll.feature.R
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayrollListScreen(
    onCreatePayroll: () -> Unit,
    onPayrollClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PayrollListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(stringResource(R.string.payroll_list_title)) }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreatePayroll) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(R.string.payroll_list_create_content_description),
                )
            }
        },
    ) { contentPadding ->
        when (val state = uiState) {
            PayrollListUiState.Loading -> LoadingState(Modifier.padding(contentPadding))

            PayrollListUiState.Empty -> EmptyState(
                title = stringResource(R.string.payroll_list_empty_title),
                message = stringResource(R.string.payroll_list_empty_message),
                actionLabel = stringResource(R.string.payroll_list_empty_action),
                onAction = onCreatePayroll,
                modifier = Modifier.padding(contentPadding),
            )

            is PayrollListUiState.Error -> ErrorState(
                message = state.message.asString(),
                modifier = Modifier.padding(contentPadding),
            )

            is PayrollListUiState.Success -> LazyColumn(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(state.payrolls, key = { it.payroll.id }) { item ->
                    PayrollListRow(item = item, onClick = { onPayrollClick(item.payroll.id) })
                }
            }
        }
    }
}

private val rowDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")

@Composable
private fun PayrollListRow(item: PayrollListItem, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = rowDateFormatter.format(item.payroll.createdAt.atZone(ZoneId.systemDefault())),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = pluralStringResource(
                    R.plurals.payroll_list_employee_count,
                    item.employeeCount,
                    item.employeeCount,
                ),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp),
            )
            MoneyText(
                money = item.totalWages,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp),
            )

            
        }
    }
}
