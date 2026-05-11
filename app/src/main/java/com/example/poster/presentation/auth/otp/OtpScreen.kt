package com.example.poster.presentation.auth.otp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.poster.presentation.common.FeatureStubScreen

@Composable
fun OtpScreen(modifier: Modifier = Modifier) {
    FeatureStubScreen(
        title = "OTP",
        description = "OTP confirmation is isolated as a separate feature package.",
        checkpoints = listOf(
            "Collect one-time code",
            "Call VerifyOtpUseCase",
            "Persist session and continue to the main area",
        ),
        modifier = modifier,
    )
}
