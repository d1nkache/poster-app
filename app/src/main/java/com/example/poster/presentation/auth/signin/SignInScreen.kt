package com.example.poster.presentation.auth.signin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.poster.presentation.auth.components.PosterAuthBackground
import com.example.poster.presentation.auth.components.PosterAuthIcons
import com.example.poster.presentation.auth.components.PosterAuthInput
import com.example.poster.presentation.auth.components.PosterBottomAuthText
import com.example.poster.presentation.auth.components.PosterLogoBlock
import com.example.poster.presentation.auth.components.PosterPrimaryButton
import com.example.poster.ui.theme.PosterTheme

@Composable
fun SignInScreen(
    onSignInClick: (email: String, password: String) -> Unit = { _, _ -> },
    onSignUpClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    PosterAuthBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 448.dp)
                .align(Alignment.Center)
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PosterLogoBlock(showSubtitle = true)

            Spacer(modifier = Modifier.height(48.dp))

            PosterAuthInput(
                value = email,
                onValueChange = { email = it },
                hint = "Email",
                icon = PosterAuthIcons.Email,
                keyboardType = KeyboardType.Email,
            )

            Spacer(modifier = Modifier.height(16.dp))

            PosterAuthInput(
                value = password,
                onValueChange = { password = it },
                hint = "Password",
                icon = PosterAuthIcons.Password,
                keyboardType = KeyboardType.Password,
                isPassword = true,
            )

            Spacer(modifier = Modifier.height(40.dp))

            PosterPrimaryButton(
                text = "Sign In",
                onClick = { onSignInClick(email, password) },
            )

            Spacer(modifier = Modifier.height(20.dp))

            PosterBottomAuthText(
                normalText = "Don't have an account?",
                actionText = "Sign Up",
                onActionClick = onSignUpClick,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun SignInScreenPreview() {
    PosterTheme {
        SignInScreen()
    }
}
