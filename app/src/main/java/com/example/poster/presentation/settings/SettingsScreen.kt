package com.example.poster.presentation.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.poster.presentation.common.FeatureStubScreen

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    FeatureStubScreen(
        title = "Settings",
        description = "Settings stays in presentation until it needs its own domain contracts.",
        checkpoints = listOf(
            "Theme and notification toggles",
            "Session exit and account actions",
            "App-level preferences backed by core storage later",
        ),
        modifier = modifier,
    )
}
