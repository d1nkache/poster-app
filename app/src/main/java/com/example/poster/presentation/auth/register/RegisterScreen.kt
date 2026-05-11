package com.example.poster.presentation.auth.register

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.poster.presentation.common.FeatureStubScreen

@Composable
fun RegisterScreen(modifier: Modifier = Modifier) {
    FeatureStubScreen(
        title = "Register",
        description = "Registration flow keeps presentation thin and delegates logic to use cases.",
        checkpoints = listOf(
            "Collect identifier, display name, and password",
            "Call RegisterUseCase",
            "Continue to OTP or chats depending on backend flow",
        ),
        modifier = modifier,
    )
}
