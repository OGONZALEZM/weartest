package com.ogonzalezm.weartest.common

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object ChatBotTextStyle {

    val title = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 32.sp
    )

    val subTitle = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 16.sp
    )

    val body = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Light,
        lineHeight = 12.sp
    )

}