package com.currencyexchange.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.click
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import com.currencyexchange.model.CurrencyCode
import com.currencyexchange.ui.theme.CurrencyExchangeTheme
import com.currencyexchange.viewmodel.CurrencyPickerItemState
import kotlinx.collections.immutable.persistentListOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

internal class CurrencyExchangeUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun GIVEN_error_state_WHEN_overlay_shown_THEN_error_message_is_displayed() {
        composeTestRule.setContent {
            CurrencyExchangeTheme { ErrorOverlay(onRetryClick = {}) }
        }

        composeTestRule.onNodeWithText("Something went wrong").assertIsDisplayed()
    }

    @Test
    fun GIVEN_error_state_WHEN_retry_button_clicked_THEN_retry_callback_is_invoked() {
        var retryClicked = false
        composeTestRule.setContent {
            CurrencyExchangeTheme { ErrorOverlay(onRetryClick = { retryClicked = true }) }
        }

        composeTestRule.onNodeWithText("Retry").performClick()

        assertTrue(retryClicked)
    }

    @Test
    fun GIVEN_action_cell_with_currency_WHEN_displayed_THEN_currency_code_is_shown() {
        composeTestRule.setContent {
            CurrencyExchangeTheme {
                ActionCell(
                    amount = "",
                    currencyCode = CurrencyCode.EUR,
                    onValueChange = {},
                )
            }
        }

        composeTestRule.onNodeWithText("EUR").assertIsDisplayed()
    }

    @Test
    fun GIVEN_action_cell_with_amount_WHEN_displayed_THEN_amount_is_shown() {
        composeTestRule.setContent {
            CurrencyExchangeTheme {
                ActionCell(
                    amount = "100.00",
                    currencyCode = CurrencyCode.EUR,
                    onValueChange = {},
                )
            }
        }

        composeTestRule.onNodeWithText("100.00").assertIsDisplayed()
    }

    @Test
    fun GIVEN_action_cell_with_picker_enabled_WHEN_cell_clicked_THEN_currency_picker_callback_is_invoked() {
        var pickerShown = false
        composeTestRule.setContent {
            CurrencyExchangeTheme {
                ActionCell(
                    amount = "",
                    currencyCode = CurrencyCode.EUR,
                    onValueChange = {},
                    onShowCurrencyPicker = { pickerShown = true },
                )
            }
        }

        composeTestRule.onNode(hasClickAction() and hasAnyDescendant(hasText("EUR")), useUnmergedTree = true)
            .performTouchInput { click(centerLeft) }

        assertTrue(pickerShown)
    }

    @Test
    fun GIVEN_currency_picker_WHEN_shown_THEN_picker_title_is_displayed() {
        composeTestRule.setContent {
            CurrencyExchangeTheme {
                CurrencyPickerBottomSheet(
                    currencyItems = persistentListOf(
                        CurrencyPickerItemState(code = CurrencyCode.EUR, isSelected = false),
                    ),
                    onTargetCurrencySelected = {},
                    onDismiss = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Choose Currency").assertIsDisplayed()
    }

    @Test
    fun GIVEN_currency_picker_WHEN_currency_item_clicked_THEN_selection_callback_is_invoked() {
        var selectedCurrency: CurrencyCode? = null
        composeTestRule.setContent {
            CurrencyExchangeTheme {
                CurrencyPickerBottomSheet(
                    currencyItems = persistentListOf(
                        CurrencyPickerItemState(code = CurrencyCode.EUR, isSelected = false),
                    ),
                    onTargetCurrencySelected = { selectedCurrency = it },
                    onDismiss = {},
                )
            }
        }

        composeTestRule.onNodeWithText("EUR").performClick()

        assertEquals(CurrencyCode.EUR, selectedCurrency)
    }
}
