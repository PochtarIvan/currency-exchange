package com.currencyexchange.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.currencyexchange.R
import com.currencyexchange.model.CurrencyCode
import com.currencyexchange.ui.theme.backgroundBrand
import com.currencyexchange.ui.theme.backgroundDefault
import com.currencyexchange.ui.theme.cellBackground
import com.currencyexchange.ui.theme.textColorDefault
import com.currencyexchange.ui.theme.textColorPlaceholder

@Composable
internal fun ActionCell(
    amount: String,
    currencyCode: CurrencyCode?,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onShowCurrencyPicker: (() -> Unit)? = null,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 4.dp,
        color = cellBackground,
    ) {
        Row(
            modifier = if (onShowCurrencyPicker != null) {
                Modifier.clickable(onClick = onShowCurrencyPicker)
            } else {
                Modifier
            },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            currencyCode?.let { code ->
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                end = 8.dp,
                            )
                            .size(16.dp),
                        painter = painterResource(code.flagRes()),
                        contentDescription = null,
                    )

                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(end = 8.dp),
                        text = currencyCode.apiCode,
                        color = textColorDefault,
                    )

                    onShowCurrencyPicker?.let {
                        Icon(
                            contentDescription = null,
                            painter = painterResource(R.drawable.ic_general_chevron_down),
                        )
                    }
                }
            }

            TextField(
                modifier = Modifier.weight(2f),
                value = amount,
                onValueChange = onValueChange,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.End,
                ),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                ),
                placeholder = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.exchange_amount_placeholder),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            textAlign = TextAlign.End,
                        ),
                        color = textColorPlaceholder,
                    )
                }
            )
        }
    }
}

@Composable
internal fun SwapIcon(
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .clickable(
            onClick = onClick,
        ),
        shape = CircleShape,
        color = backgroundDefault,
        shadowElevation = 2.dp
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(backgroundBrand)
            )
        }
    }
    Image(
        contentDescription = null,
        painter = painterResource(R.drawable.ic_general_arrow_down),
    )
}

@Composable
internal fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = backgroundBrand,
        )
    }
}

@Composable
internal fun ErrorOverlay(
    onRetryClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(R.string.overlay_error_text),
                color = MaterialTheme.colorScheme.error,
            )

            Button(
                onClick = onRetryClick,
            ) {
                Text(
                    text = stringResource(R.string.overlay_retry)
                )
            }
        }
    }
}

@DrawableRes
internal fun CurrencyCode.flagRes(): Int = when (this) {
    CurrencyCode.USD_C -> R.drawable.flag_usd
    CurrencyCode.MXN -> R.drawable.flag_mxn
    CurrencyCode.EUR -> R.drawable.flag_eur
    CurrencyCode.BRL -> R.drawable.flag_brl
    CurrencyCode.COP -> R.drawable.flag_cop
    CurrencyCode.ARS -> R.drawable.flag_ars
}

@Preview
@Composable
private fun ActionCellPreview() {
    ActionCell(
        amount = "100.00",
        currencyCode = CurrencyCode.USD_C,
        onValueChange = {},
    )
}