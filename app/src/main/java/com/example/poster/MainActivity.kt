package com.example.poster

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.poster.presentation.auth.otp.VerifyEmailScreen
import com.example.poster.presentation.auth.register.RegisterScreen
import com.example.poster.presentation.auth.signin.SignInScreen
import com.example.poster.presentation.chats.ChatListScreen
import com.example.poster.presentation.chats.sampleChatPreviews
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

                LaunchedEffect(Unit) {
                    Log.d(TAG, "Compose content loaded")
                    Log.d(TAG, "Auth placeholder check started")
                    Log.d(TAG, "isAuthorized = $isAuthorized")
                    Log.d(TAG, "Initial screen = $currentScreen")

                    if (isAuthorized) {
                        Log.d(TAG, "User is authorized, opening ChatListScreen")
                    } else {
                        Log.d(TAG, "User is not authorized, opening auth screens")
                    }
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
                                // TODO: navigate to MessagesScreen(chat.id)
                            },
                            onSetupTokenClick = {
                                Log.d(TAG, "Setup token clicked")
                                // TODO: navigate to mail access token setup
                            },
                            onSettingsClick = {
                                Log.d(TAG, "Settings clicked")
                                // TODO: navigate to SettingsScreen
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
}
