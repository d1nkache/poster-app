package com.example.poster

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.poster.presentation.auth.otp.VerifyEmailScreen
import com.example.poster.presentation.auth.register.RegisterScreen
import com.example.poster.presentation.auth.signin.SignInScreen
import com.example.poster.presentation.chats.ChatListScreen
import com.example.poster.presentation.chats.ChatPreviewUi
import com.example.poster.presentation.chats.sampleChatPreviews
import com.example.poster.presentation.messages.MessageUi
import com.example.poster.presentation.messages.MessagesScreen
import com.example.poster.presentation.messages.sampleMessages
import com.example.poster.presentation.profile.ProfileScreen
import com.example.poster.presentation.settings.AccessTokenSettingsScreen
import com.example.poster.presentation.settings.SettingsScreen
import com.example.poster.ui.theme.PosterTheme

class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "PosterMainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate: app started")
        enableEdgeToEdge()

        setContent {
            PosterTheme {
                /*
                 * PLACEHOLDER AUTH LOGIC
                 *
                 * Сейчас всегда false.
                 * Поэтому приложение по дефолту открывает Sign In / Sign Up.
                 *
                 * Потом здесь будет проверка:
                 * - есть ли JWT access token
                 * - не истёк ли токен
                 * - нужно ли отправлять пользователя на Login/Register
                 */
                val isAuthorized = false

                var currentScreen by rememberSaveable {
                    mutableStateOf(
                        if (isAuthorized) {
                            AppScreen.CHATS
                        } else {
                            AppScreen.SIGN_IN
                        }
                    )
                }
                var verificationEmail by rememberSaveable {
                    mutableStateOf("user@mail.ru")
                }
                var selectedChat by remember {
                    mutableStateOf<ChatPreviewUi?>(null)
                }
                var messages by remember {
                    mutableStateOf(sampleMessages)
                }
                var accessTokenBackScreen by rememberSaveable {
                    mutableStateOf(AppScreen.CHATS)
                }

                LaunchedEffect(Unit) {
                    Log.d(TAG, "Compose content loaded")
                    Log.d(TAG, "Auth placeholder check started")
                    Log.d(TAG, "isAuthorized = $isAuthorized")
                }

                LaunchedEffect(currentScreen) {
                    Log.d(TAG, "Current screen = $currentScreen")
                }

                when (currentScreen) {
                    AppScreen.SIGN_IN -> {
                        SignInScreen(
                            onSignInClick = { email, _ ->
                                Log.d(TAG, "Sign In clicked: email=$email")
                                Log.d(TAG, "Placeholder sign in success, opening chats")
                                currentScreen = AppScreen.CHATS
                            },
                            onSignUpClick = {
                                Log.d(TAG, "Navigate to Sign Up")
                                currentScreen = AppScreen.SIGN_UP
                            },
                        )
                    }

                    AppScreen.SIGN_UP -> {
                        RegisterScreen(
                            onContinueClick = { name, email, _ ->
                                Log.d(TAG, "Sign Up clicked: name=$name, email=$email")
                                verificationEmail = email.ifBlank { "user@mail.ru" }
                                Log.d(TAG, "Opening VerifyEmailScreen for $verificationEmail")
                                currentScreen = AppScreen.VERIFY_EMAIL
                            },
                            onSignInClick = {
                                Log.d(TAG, "Navigate to Sign In")
                                currentScreen = AppScreen.SIGN_IN
                            },
                        )
                    }

                    AppScreen.VERIFY_EMAIL -> {
                        VerifyEmailScreen(
                            email = verificationEmail,
                            onVerifyClick = { code ->
                                Log.d(TAG, "Verify code clicked: code=$code")
                                Log.d(TAG, "Placeholder verification success, opening chats")
                                currentScreen = AppScreen.CHATS
                            },
                            onResendClick = {
                                Log.d(TAG, "Resend code clicked for $verificationEmail")
                            },
                        )
                    }

                    AppScreen.CHATS -> {
                        ChatListScreen(
                            chats = sampleChatPreviews,
                            isSetupRequired = true,
                            onChatClick = { chat ->
                                Log.d(TAG, "Chat clicked: id=${chat.id}, name=${chat.name}")
                                selectedChat = chat
                                currentScreen = AppScreen.MESSAGES
                            },
                            onSetupTokenClick = {
                                Log.d(TAG, "Setup token clicked")
                                accessTokenBackScreen = AppScreen.CHATS
                                currentScreen = AppScreen.ACCESS_TOKEN_SETTINGS
                            },
                            onSettingsClick = {
                                Log.d(TAG, "Settings clicked")
                                currentScreen = AppScreen.SETTINGS
                            },
                        )
                    }

                    AppScreen.SETTINGS -> {
                        SettingsScreen(
                            name = "Your Name",
                            username = "@your.username",
                            email = "your.email@example.com",
                            birthday = "January 1, 2000",
                            bio = "Hey there! I'm using Poster.",
                            accessTokenStatus = "Configured",
                            onBackClick = {
                                Log.d(TAG, "Back from Settings")
                                currentScreen = AppScreen.CHATS
                            },
                            onAccessTokenClick = {
                                Log.d(TAG, "Open Access Token Settings")
                                accessTokenBackScreen = AppScreen.SETTINGS
                                currentScreen = AppScreen.ACCESS_TOKEN_SETTINGS
                            },
                            onPrivacyClick = {
                                Log.d(TAG, "Privacy & Security clicked")
                            },
                            onLanguageClick = {
                                Log.d(TAG, "Language clicked")
                            },
                            onProfilePhotoClick = {
                                Log.d(TAG, "Profile photo clicked")
                            },
                            onAccountItemClick = { itemId ->
                                Log.d(TAG, "Account item clicked: $itemId")
                            },
                        )
                    }

                    AppScreen.ACCESS_TOKEN_SETTINGS -> {
                        AccessTokenSettingsScreen(
                            initialToken = "",
                            onBackClick = {
                                Log.d(TAG, "Back from AccessTokenSettings")
                                currentScreen = accessTokenBackScreen
                            },
                            onSaveTokenClick = { token ->
                                Log.d(TAG, "Access token saved placeholder, length=${token.length}")
                                /*
                                 * PLACEHOLDER:
                                 * Later this should call SaveMailAccessTokenUseCase(token),
                                 * persist through encrypted storage, and refresh hasMailAccessToken.
                                 */
                                currentScreen = accessTokenBackScreen
                            },
                            onHowToGetTokenClick = {
                                Log.d(TAG, "How to get access token clicked")
                                /*
                                 * PLACEHOLDER:
                                 * Later this can open a bottom sheet or web guide for provider-specific
                                 * app passwords: Gmail, Yandex, Mail.ru, and others.
                                 */
                            },
                        )
                    }

                    AppScreen.MESSAGES -> {
                        val chat = selectedChat ?: sampleChatPreviews.first()

                        MessagesScreen(
                            contactName = chat.name,
                            contactInitials = chat.initials,
                            messages = messages,
                            onBackClick = {
                                Log.d(TAG, "Back from Messages")
                                currentScreen = AppScreen.CHATS
                            },
                            onProfileClick = {
                                Log.d(TAG, "Open Profile from Messages")
                                currentScreen = AppScreen.PROFILE
                            },
                            onMoreClick = {
                                Log.d(TAG, "More clicked in Messages")
                            },
                            onSendClick = { text ->
                                Log.d(TAG, "Send message: $text")
                                messages = messages + MessageUi(
                                    id = System.currentTimeMillis().toString(),
                                    text = text,
                                    time = "Now",
                                    isMine = true,
                                )
                            },
                        )
                    }

                    AppScreen.PROFILE -> {
                        val chat = selectedChat ?: sampleChatPreviews.first()

                        ProfileScreen(
                            initials = chat.initials,
                            name = chat.name,
                            username = "@${chat.name.lowercase().replace(" ", ".")}",
                            bio = "Product designer passionate about creating beautiful and functional user experiences.",
                            onBackClick = {
                                Log.d(TAG, "Back from Profile")
                                currentScreen = AppScreen.MESSAGES
                            },
                        )
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}

private enum class AppScreen {
    SIGN_IN,
    SIGN_UP,
    VERIFY_EMAIL,
    CHATS,
    SETTINGS,
    ACCESS_TOKEN_SETTINGS,
    MESSAGES,
    PROFILE,
}
