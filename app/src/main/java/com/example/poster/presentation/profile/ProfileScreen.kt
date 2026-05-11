package com.example.poster.presentation.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.poster.presentation.common.FeatureStubScreen

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    FeatureStubScreen(
        title = "Profile",
        description = "Profile is modeled independently from auth to keep responsibilities narrow.",
        checkpoints = listOf(
            "Load profile by user id",
            "Update bio, avatar, or status with UpdateProfileUseCase",
            "Render immutable UI state from the ViewModel layer later",
        ),
        modifier = modifier,
    )
}
