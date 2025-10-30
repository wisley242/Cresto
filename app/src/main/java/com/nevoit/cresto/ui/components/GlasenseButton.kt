package com.nevoit.cresto.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousCapsule

@Composable
fun GlasenseButton(
    enabled: Boolean = true,
    shape: Shape = ContinuousCapsule,
    onClick: () -> Unit,
    modifier: Modifier,
    colors: ButtonColors,
    animated: Boolean = true,
    content: @Composable () -> Unit,
) {
    val contentColor = if (enabled) colors.contentColor else colors.disabledContentColor
    val backgroundColor = if (enabled) colors.containerColor else colors.disabledContainerColor
    val interactionSource = remember { MutableInteractionSource() }

    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.2f else 1.0f,
        animationSpec = spring(0.5f, 300f, 0.0001f)
    )
    val alpha by animateFloatAsState(
        targetValue = if (isPressed) 0.2f else 0f,
        animationSpec = spring(0.5f, 300f, 0.001f)
    )
    Box(
        modifier = Modifier
            .then(modifier)
            .then(
                if (animated) Modifier.graphicsLayer {
                    scaleY = scale
                    scaleX = scale
                    transformOrigin = TransformOrigin.Center
                } else Modifier
            )
            .then(if (animated) Modifier.clip(shape) else Modifier)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                onClick = { onClick() },
                indication = null,
                role = Role.Button
            )
            .height(48.dp)
            .background(color = backgroundColor, shape = shape)
            .then(
                if (animated) {
                    Modifier.drawBehind {
                        drawRect(
                            size = this.size,
                            color = Color.White,
                            alpha = alpha,
                            blendMode = BlendMode.Plus
                        )
                    }
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            content()
        }
    }
}

@Composable
fun GlasenseButtonAlt(
    enabled: Boolean = true,
    shape: Shape = ContinuousCapsule,
    onClick: () -> Unit,
    modifier: Modifier,
    colors: ButtonColors,
    indication: Boolean = true,
    content: @Composable () -> Unit,
) {
    val contentColor = if (enabled) colors.contentColor else colors.disabledContentColor
    val backgroundColor = if (enabled) colors.containerColor else colors.disabledContainerColor
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                onClick = { onClick() },
                indication = null,
                role = Role.Button
            )
            .then(modifier)
            .height(48.dp)
            .background(color = backgroundColor, shape = shape),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            content()
        }
    }
}

@Composable
fun GlasenseButtonAdaptable(
    width: () -> Dp,
    height: () -> Dp,
    padding: PaddingValues,
    tint: Color? = null,
    enabled: Boolean = true,
    shape: Shape = ContinuousCapsule,
    onClick: () -> Unit,
    modifier: Modifier,
    colors: ButtonColors,
    animated: Boolean = true,
    content: @Composable () -> Unit,
) {
    val contentColor = tint ?: if (enabled) colors.contentColor else colors.disabledContentColor
    val backgroundColor = if (enabled) colors.containerColor else colors.disabledContainerColor
    val interactionSource = remember { MutableInteractionSource() }

    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.2f else 1.0f,
        animationSpec = spring(0.5f, 300f, 0.0001f)
    )
    val alpha by animateFloatAsState(
        targetValue = if (isPressed) 0.2f else 0f,
        animationSpec = spring(0.5f, 300f, 0.001f)
    )
    Box(
        modifier = Modifier
            .padding(padding)
            .width(width())
            .height(height())
            .then(
                if (animated) Modifier.graphicsLayer {
                    scaleY = scale
                    scaleX = scale
                    transformOrigin = TransformOrigin.Center
                } else Modifier
            )
            .then(modifier)
            .then(if (animated) Modifier.clip(shape) else Modifier)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                onClick = { onClick() },
                indication = null,
                role = Role.Button
            )
            .height(48.dp)
            .background(color = backgroundColor, shape = shape)
            .then(
                if (animated) {
                    Modifier.drawBehind {
                        drawRect(
                            size = this.size,
                            color = Color.White,
                            alpha = alpha,
                            blendMode = BlendMode.Plus
                        )
                    }
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            content()
        }
    }
}