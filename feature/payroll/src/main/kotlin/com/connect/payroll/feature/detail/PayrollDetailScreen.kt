package com.connect.payroll.feature.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.connect.payroll.core.designsystem.component.EmptyState
import com.connect.payroll.core.designsystem.component.ErrorState
import com.connect.payroll.core.designsystem.component.LoadingState
import com.connect.payroll.core.designsystem.component.MoneyText
import com.connect.payroll.core.designsystem.text.asString
import com.connect.payroll.feature.domain.model.PayrollDetail
import com.connect.payroll.feature.domain.model.PayrollLineItem
import com.connect.payroll.feature.domain.model.PayrollSummary
import com.connect.payroll.core.model.Money
import com.connect.payroll.feature.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayrollDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PayrollDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.payroll_detail_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.payroll_detail_back_content_description),
                        )
                    }
                },
            )
        },
    ) { contentPadding ->
        when (val state = uiState) {
            PayrollDetailUiState.Loading -> LoadingState(Modifier.padding(contentPadding))

            PayrollDetailUiState.Empty -> EmptyState(
                title = stringResource(R.string.payroll_detail_not_found_title),
                message = stringResource(R.string.payroll_detail_not_found_message),
                modifier = Modifier.padding(contentPadding),
            )

            is PayrollDetailUiState.Error -> ErrorState(
                message = state.message.asString(),
                modifier = Modifier.padding(contentPadding),
            )

            is PayrollDetailUiState.Success -> PayrollDetailContent(
                detail = state.detail,
                modifier = Modifier.padding(contentPadding),
            )
        }
    }
}

@Composable
private fun PayrollDetailContent(detail: PayrollDetail, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f, fill = true),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(detail.lineItems, key = { it.employee.id }) { lineItem ->
                EmployeeLineCard(lineItem)
            }
        }
        SummaryCard(detail.summary)
    }
}

@Composable
private fun EmployeeLineCard(lineItem: PayrollLineItem) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    lineItem.employee.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f),
                )
                if (lineItem.employee.isExempt) {
                    ExemptedView()
                }
            }
            LabeledMoneyRow(label = stringResource(R.string.payroll_detail_label_total), money = lineItem.employee.wages)
            LabeledMoneyRow(label = stringResource(R.string.payroll_detail_label_taxes), money = lineItem.tax)
            LabeledMoneyRow(label = stringResource(R.string.payroll_detail_label_net), money = lineItem.net)
        }
    }
}

@Composable
private fun SummaryCard(summary: PayrollSummary) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(stringResource(R.string.payroll_detail_summary_title), style = MaterialTheme.typography.titleLarge)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            LabeledMoneyRow(label = stringResource(R.string.payroll_detail_label_total), money = summary.totalWages)
            if (summary.totalTaxes > Money.ZERO) {
                LabeledMoneyRow(label = stringResource(R.string.payroll_detail_label_total_taxes), money = summary.totalTaxes)
            }
            LabeledMoneyRow(label = stringResource(R.string.payroll_detail_label_total_net), money = summary.totalNet)
        }
    }
}

@Composable
private fun LabeledMoneyRow(label: String, money: Money) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        MoneyText(money = money, style = MaterialTheme.typography.bodyLarge)
    }
}


@Composable
private fun ExemptedView(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            tint = MaterialTheme.colorScheme.tertiary,
            contentDescription = null,
            modifier = Modifier.size(10.dp),
        )
        Text(
            text = stringResource(R.string.create_payroll_exempt_label),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(start = 2.dp),
        )
    }
}
