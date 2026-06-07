package com.example.poster.presentation.auth.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val PosterBackground = Color(0xFF030306)
val PosterSurface = Color(0xFF1B1A31)
val PosterStroke = Color(0xFF302B68)
val PosterPrimary = Color(0xFF625BFF)
val PosterPrimaryDark = Color(0xFF4538C8)
val PosterTextPrimary = Color(0xFFF6F6FF)
val PosterTextSecondary = Color(0xFFA2A3B8)
val PosterTextMuted = Color(0xFF7B7D92)
val PosterIcon = Color(0xFF7F8BFF)

private val AuthMotionEasing = CubicBezierEasing(0.2f, 0f, 0f, 1f)

@Composable
fun PosterAuthBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF10102A),
                        PosterBackground,
                        Color(0xFF070414),
                    ),
                    radius = 1250f,
                )
            ),
        content = content,
    )
}

@Composable
fun PosterLogoBlock(
    showSubtitle: Boolean,
    modifier: Modifier = Modifier,
) {
    androidx.compose.foundation.layout.Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Poster",
            color = PosterPrimary,
            fontSize = 44.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (-1).sp,
        )

        if (showSubtitle) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Secure messaging via SMTP/IMAP",
                color = PosterTextSecondary,
                fontSize = 15.sp,
            )
        }
    }
}

@Composable
fun PosterAuthInput(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
) {
    Row(
        modifier = modifier
            .height(58.dp)
            .fillMaxWidth()
            .background(
                color = PosterSurface,
                shape = RoundedCornerShape(13.dp),
            )
            .border(
                width = 1.dp,
                color = PosterStroke,
                shape = RoundedCornerShape(13.dp),
            )
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PosterIcon,
            modifier = Modifier.size(21.dp),
        )

        Spacer(modifier = Modifier.width(14.dp))

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            cursorBrush = SolidColor(PosterPrimary),
            textStyle = TextStyle(
                color = PosterTextPrimary,
                fontSize = 15.sp,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (isPassword) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = hint,
                            color = PosterTextMuted,
                            fontSize = 15.sp,
                        )
                    }
                    innerTextField()
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun PosterPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(13.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
        ),
        contentPadding = PaddingValues(),
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.6f)
            .shadow(
                elevation = 28.dp,
                shape = RoundedCornerShape(13.dp),
                spotColor = PosterPrimary.copy(alpha = 0.45f),
            )
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(PosterPrimary, PosterPrimaryDark),
                ),
                shape = RoundedCornerShape(13.dp),
            ),
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun PosterBottomAuthText(
    normalText: String,
    actionText: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = normalText,
            color = PosterTextMuted,
            fontSize = 15.sp,
        )
        TextButton(
            onClick = onActionClick,
            contentPadding = PaddingValues(horizontal = 4.dp),
        ) {
            Text(
                text = actionText,
                color = PosterIcon,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun PosterOtpCell(
    value: String,
    isActive: Boolean,
    modifier: Modifier = Modifier,
) {
    val cellScale by animateFloatAsState(
        targetValue = when {
            isActive -> 1.025f
            value.isNotEmpty() -> 1.015f
            else -> 1f
        },
        animationSpec = tween(
            durationMillis = 220,
            easing = AuthMotionEasing,
        ),
        label = "otpCellScale",
    )
    val cellElevation by animateDpAsState(
        targetValue = if (isActive || value.isNotEmpty()) 18.dp else 0.dp,
        animationSpec = tween(
            durationMillis = 240,
            easing = AuthMotionEasing,
        ),
        label = "otpCellElevation",
    )
    val borderColor by animateColorAsState(
        targetValue = when {
            isActive -> PosterPrimary
            value.isNotEmpty() -> PosterPrimary.copy(alpha = 0.72f)
            else -> PosterStroke
        },
        animationSpec = tween(
            durationMillis = 220,
            easing = AuthMotionEasing,
        ),
        label = "otpBorderColor",
    )
    val glowColor by animateColorAsState(
        targetValue = when {
            isActive -> PosterPrimary.copy(alpha = 0.32f)
            value.isNotEmpty() -> PosterPrimary.copy(alpha = 0.18f)
            else -> Color.Transparent
        },
        animationSpec = tween(
            durationMillis = 240,
            easing = AuthMotionEasing,
        ),
        label = "otpGlowColor",
    )

    Box(
        modifier = modifier
            .shadow(
                elevation = cellElevation,
                shape = RoundedCornerShape(13.dp),
                spotColor = glowColor,
            )
            .scale(cellScale)
            .background(
                color = PosterSurface,
                shape = RoundedCornerShape(13.dp),
            )
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(13.dp),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = value,
            color = PosterTextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
    }
}

object PosterAuthIcons {
    val Name = Icons.Outlined.Person
    val Email = Icons.Outlined.Email
    val Password = Icons.Outlined.Lock
}
