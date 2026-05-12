package com.example.poster.presentation.auth.otp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import com.example.poster.presentation.auth.components.PosterAuthBackground
import com.example.poster.presentation.auth.components.PosterBottomAuthText
import com.example.poster.presentation.auth.components.PosterLogoBlock
import com.example.poster.presentation.auth.components.PosterOtpCell
import com.example.poster.presentation.auth.components.PosterPrimaryButton
import com.example.poster.presentation.auth.components.PosterPrimary
import com.example.poster.presentation.auth.components.PosterTextPrimary
import com.example.poster.presentation.auth.components.PosterTextSecondary
import com.example.poster.ui.theme.PosterTheme

@Composable
fun VerifyEmailScreen(
    email: String,
    onVerifyClick: (code: String) -> Unit = {},
    onResendClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var code by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }

    PosterAuthBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 448.dp)
                .align(Alignment.Center)
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PosterLogoBlock(showSubtitle = false)

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Verify Your Email",
                color = PosterTextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Enter the 6-digit code sent to",
                color = PosterTextSecondary,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = email,
                color = PosterTextPrimary,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(34.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                    ) {
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    },
                contentAlignment = Alignment.Center,
            ) {
                BasicTextField(
                    value = code,
                    onValueChange = { newValue ->
                        val filtered = newValue.filter(Char::isDigit).take(6)
                        code = filtered

                        if (filtered.length == 6) {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                    ),
                    cursorBrush = SolidColor(PosterPrimary),
                    textStyle = TextStyle(
                        color = Color.Transparent,
                        fontSize = 1.sp,
                    ),
                    modifier = Modifier
                        .matchParentSize()
                        .focusRequester(focusRequester),
                )

                androidx.compose.foundation.layout.Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    repeat(6) { index ->
                        PosterOtpCell(
                            value = code.getOrNull(index)?.toString().orEmpty(),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(34.dp))

            PosterPrimaryButton(
                text = "Verify",
                enabled = code.length == 6,
                onClick = { onVerifyClick(code) },
            )

            Spacer(modifier = Modifier.height(22.dp))

            PosterBottomAuthText(
                normalText = "Didn't receive the code?",
                actionText = "Resend",
                onActionClick = onResendClick,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun VerifyEmailScreenPreview() {
    PosterTheme {
        VerifyEmailScreen(email = "user@mail.ru")
    }
}
