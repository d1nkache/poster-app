package com.example.poster.presentation.navigation

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.poster.presentation.auth.login.LoginScreen
import com.example.poster.presentation.auth.otp.OtpScreen
import com.example.poster.presentation.auth.register.RegisterScreen
import com.example.poster.presentation.chats.ChatsScreen
import com.example.poster.presentation.messages.MessagesScreen
import com.example.poster.presentation.profile.ProfileScreen
import com.example.poster.presentation.settings.SettingsScreen

private val authDestinations = listOf(
    PosterDestination.LOGIN,
    PosterDestination.REGISTER,
    PosterDestination.OTP,
)

private val mainDestinations = listOf(
    PosterDestination.CHATS,
    PosterDestination.MESSAGES,
    PosterDestination.PROFILE,
    PosterDestination.SETTINGS,
)

@Composable
fun PosterApp() {
    var currentDestination by rememberSaveable {
        mutableStateOf(PosterDestination.CHATS)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = currentDestination.title)
                }
            )
        },
        bottomBar = {
            NavigationBar {
                mainDestinations.forEach { destination ->
                    NavigationBarItem(
                        selected = currentDestination == destination,
                        onClick = { currentDestination = destination },
                        icon = { Text(destination.title.take(1)) },
                        label = { Text(destination.title) },
                    )
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            AuthShortcutRow(
                currentDestination = currentDestination,
                onDestinationSelected = { currentDestination = it },
            )

            HorizontalDivider()

            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                when (currentDestination) {
                    PosterDestination.LOGIN -> LoginScreen()
                    PosterDestination.REGISTER -> RegisterScreen()
                    PosterDestination.OTP -> OtpScreen()
                    PosterDestination.CHATS -> ChatsScreen()
                    PosterDestination.MESSAGES -> MessagesScreen()
                    PosterDestination.PROFILE -> ProfileScreen()
                    PosterDestination.SETTINGS -> SettingsScreen()
                }
            }
        }
    }
}

@Composable
private fun AuthShortcutRow(
    currentDestination: PosterDestination,
    onDestinationSelected: (PosterDestination) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        authDestinations.forEach { destination ->
            OutlinedButton(
                onClick = { onDestinationSelected(destination) },
                enabled = currentDestination != destination,
            ) {
                Text(text = destination.title)
            }
        }
    }
}
