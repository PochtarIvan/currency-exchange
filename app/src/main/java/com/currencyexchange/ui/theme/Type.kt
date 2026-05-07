package com.currencyexchange.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(

    titleLarge = TextStyle( // H1
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    ),

    titleMedium = TextStyle( // H2
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp
    ),

    bodyLarge = TextStyle( // P1
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),

    bodyMedium = TextStyle( // P2
        fontSize = 14.sp
    )

    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)