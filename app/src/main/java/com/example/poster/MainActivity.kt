package com.example.poster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.poster.presentation.auth.otp.VerifyEmailScreen
import com.example.poster.presentation.auth.register.RegisterScreen
import com.example.poster.ui.theme.PosterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PosterTheme {
                var pendingEmail by rememberSaveable {
                    mutableStateOf<String?>(null)
                }

                val email = pendingEmail
                if (email == null) {
                    RegisterScreen(
                        onContinueClick = { _, enteredEmail, _ ->
                            pendingEmail = enteredEmail
                        },
                        onSignInClick = {},
                    )
                } else {
                    VerifyEmailScreen(
                        email = email,
                        onVerifyClick = {},
                        onResendClick = {},
                    )
                }
            }
        }
    }
}
