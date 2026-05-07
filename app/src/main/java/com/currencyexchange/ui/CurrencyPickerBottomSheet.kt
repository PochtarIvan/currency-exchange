package com.currencyexchange.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.currencyexchange.viewmodel.CurrencyPickerItemState
import com.currencyexchange.R
import com.currencyexchange.model.CurrencyCode
import com.currencyexchange.ui.theme.CurrencyExchangeTheme
import com.currencyexchange.ui.theme.backgroundBrand
import com.currencyexchange.ui.theme.backgroundDefault
import com.currencyexchange.ui.theme.border
import com.currencyexchange.ui.theme.cellBackground
import com.currencyexchange.ui.theme.textColorDefault
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CurrencyPickerBottomSheet(
    currencyItems: ImmutableList<CurrencyPickerItemState>,
    onTargetCurrencySelected: (CurrencyCode) -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = backgroundDefault,
        dragHandle = {
            CurrencyPickerBottomSheetHeader(
                onDismiss = onDismiss,
            )
        },
        scrimColor = Color.Black.copy(alpha = 0.5f)
    ) {
        Surface(
            modifier = modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 4.dp,
            color = cellBackground,
        ) {
            LazyColumn {
                items(
                    items = currencyItems,
                    key = { it.code },
                ) { item ->
                    ActionRow(
                        currencyItemState = item,
                        onSelected = { onTargetCurrencySelected(item.code) },
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionRow(
    currencyItemState: CurrencyPickerItemState,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(62.dp)
            .clickable(onClick = onSelected),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(backgroundDefault),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(currencyItemState.code.flagRes()),
                    contentDescription = null,
                )
            }
            Text(
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp),
                text = currencyItemState.code.apiCode,
                color = textColorDefault,
            )
        }

        CustomRadio(
            selected = currencyItemState.isSelected,
        )
    }
}

@Composable
private fun CustomRadio(
    selected: Boolean,
) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(
                if (selected) backgroundBrand else Color.Transparent
            )
            .border(
                width = if (selected) 0.dp else 2.dp,
                color = border,
                shape = CircleShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Icon(
                painter = painterResource(R.drawable.ic_general_tick),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

@Composable
private fun CurrencyPickerBottomSheetHeader(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(
            modifier = Modifier
                .width(36.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(100))
                .background(Color(0xFFD0D0D0))
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp,),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.currency_picker_title),
                style = MaterialTheme.typography.titleMedium,
                color = textColorDefault
            )

            IconButton(
                onClick = onDismiss
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_general_cross),
                    contentDescription = null,
                    tint = textColorDefault,
                )
            }
        }
    }
}

@Preview
@Composable
internal fun CurrencyPickerBottomSheetPreview() {
    CurrencyExchangeTheme {
        CurrencyPickerBottomSheet(
            currencyItems = persistentListOf(
                CurrencyPickerItemState(
                    code = CurrencyCode.MXN,
                    isSelected = true
                ),
                CurrencyPickerItemState(
                    code = CurrencyCode.EUR,
                    isSelected = false
                ),
                CurrencyPickerItemState(
                    code = CurrencyCode.BRL,
                    isSelected = false
                ),
                CurrencyPickerItemState(
                    code = CurrencyCode.COP,
                    isSelected = false
                ),
                CurrencyPickerItemState(
                    code = CurrencyCode.ARS,
                    isSelected = false
                ),
            ),
            onDismiss = {},
            onTargetCurrencySelected = {},
        )
    }
}
