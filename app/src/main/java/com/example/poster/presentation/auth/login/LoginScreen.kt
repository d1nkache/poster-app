package com.example.poster.presentation.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.poster.presentation.common.FeatureStubScreen

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    FeatureStubScreen(
        title = "Login",
        description = "Entry point for sign in and session bootstrap.",
        checkpoints = listOf(
            "Collect identifier and password",
            "Trigger LoginUseCase from a future ViewModel",
            "Redirect to chats after successful authentication",
        ),
        modifier = modifier,
    )
}
