package com.example.poster.presentation.auth.otp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OtpScreen(modifier: Modifier = Modifier) {
    VerifyEmailScreen(
        email = "user@mail.ru",
        modifier = modifier,
    )
}
