package com.example.poster.presentation.messages

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.poster.presentation.common.FeatureStubScreen

@Composable
fun MessagesScreen(modifier: Modifier = Modifier) {
    FeatureStubScreen(
        title = "Messages",
        description = "This package will own a single conversation screen and its message composer.",
        checkpoints = listOf(
            "Observe messages by chat id",
            "Send text or attachments through SendMessageUseCase",
            "Keep UI state separate from repository implementation details",
        ),
        modifier = modifier,
    )
}
