package com.example.poster.presentation.auth.otp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalDensity
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
import com.example.poster.presentation.auth.components.PosterPrimary
import com.example.poster.presentation.auth.components.PosterOtpCell
import com.example.poster.presentation.auth.components.PosterPrimaryButton
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
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val isKeyboardVisible = WindowInsets.ime.getBottom(density) > 0

    PosterAuthBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 448.dp)
                .align(Alignment.TopCenter)
                .verticalScroll(scrollState)
                .imePadding()
                .padding(
                    start = 28.dp,
                    end = 28.dp,
                    top = if (isKeyboardVisible) 48.dp else 92.dp,
                    bottom = 32.dp,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PosterLogoBlock(showSubtitle = false)

            Spacer(modifier = Modifier.height(if (isKeyboardVisible) 28.dp else 48.dp))

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

            Spacer(modifier = Modifier.height(if (isKeyboardVisible) 24.dp else 34.dp))

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                    ) {
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    },
            ) {
                val cellSpacing = 10.dp
                val cellSize = ((maxWidth - cellSpacing * 5) / 6)
                    .coerceAtMost(56.dp)
                    .coerceAtLeast(42.dp)
                val activeIndex = code.length.coerceAtMost(5)

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
                    singleLine = true,
                    cursorBrush = SolidColor(PosterPrimary),
                    textStyle = TextStyle(
                        color = Color.Transparent,
                        fontSize = 1.sp,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(cellSize)
                        .focusRequester(focusRequester),
                )

                androidx.compose.foundation.layout.Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = cellSpacing,
                        alignment = Alignment.CenterHorizontally,
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    repeat(6) { index ->
                        PosterOtpCell(
                            value = code.getOrNull(index)?.toString().orEmpty(),
                            isActive = index == activeIndex && code.length < 6,
                            modifier = Modifier.size(cellSize),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(if (isKeyboardVisible) 24.dp else 34.dp))

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
