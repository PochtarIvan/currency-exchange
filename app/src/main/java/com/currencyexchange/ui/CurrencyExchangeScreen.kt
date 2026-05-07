package com.currencyexchange.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.currencyexchange.viewmodel.CurrencyCellType
import com.currencyexchange.viewmodel.CurrencyViewModel
import com.currencyexchange.R
import com.currencyexchange.ui.theme.CurrencyExchangeTheme
import com.currencyexchange.ui.theme.backgroundDefault
import com.currencyexchange.ui.theme.textColorBrand
import com.currencyexchange.ui.theme.textColorDefault
import com.currencyexchange.viewmodel.CurrencyExchangeUiState
import com.currencyexchange.viewmodel.Overlay

@Composable
internal fun CurrencyExchangeScreen(
    modifier: Modifier = Modifier,
    viewModel: CurrencyViewModel = hiltViewModel(),
) {
    val state = viewModel.uiState

    Column(
        modifier = modifier
            .background(backgroundDefault)
            .fillMaxSize()
            .padding(
                horizontal = 16.dp,
                vertical = 24.dp,
            ),
    ) {
        Spacer(Modifier.size(44.dp))
        Text(
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            text = stringResource(R.string.exchange_title),
            color = textColorDefault,
        )

        Text(
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            text = viewModel.rateFormatted,
            color = textColorBrand,
        )

        ActionCellsSection(
            state = state,
            viewModel = viewModel,
        )
    }

    when (state.overlay) {
        is Overlay.CurrencyPicker -> CurrencyPickerBottomSheet(
            currencyItems = viewModel.currencyPickerItems,
            onTargetCurrencySelected = { currency ->
                viewModel.onTargetCurrencySelected(currency)
            },
            onDismiss = {
                viewModel.onCurrencyPickerDismissed()
            }
        )
        is Overlay.Loading -> LoadingOverlay()
        is Overlay.Error -> ErrorOverlay { viewModel.onRetryClicked() }
        null -> Unit
    }
}

@Composable
private fun ActionCellsSection(
    state: CurrencyExchangeUiState,
    viewModel: CurrencyViewModel,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        LazyColumn(
            userScrollEnabled = false,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(
                items = state.actionCells,
                key = { it },
            ) { type ->
                when (type) {
                    CurrencyCellType.Source -> {
                        ActionCell(
                            amount = state.sourceAmount,
                            onValueChange = { viewModel.onSourceAmountChanged(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(66.dp)
                                .animateItem(),
                            currencyCode = state.sourceCurrency,
                        )
                    }

                    CurrencyCellType.Target -> {
                        ActionCell(
                            amount = state.targetAmount,
                            onValueChange = { viewModel.onTargetAmountChanged(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(66.dp)
                                .animateItem(),
                            currencyCode = state.targetCurrency,
                            onShowCurrencyPicker = { viewModel.onCurrencyPickerShown() }
                        )
                    }
                }
            }
        }

        SwapIcon(
            onClick = { viewModel.onSwapClicked() }
        )
    }
}



@Preview(showBackground = true)
@Composable
private fun CurrencyExchangeScreenPreview() {
    CurrencyExchangeTheme {
        CurrencyExchangeScreen()
    }
}