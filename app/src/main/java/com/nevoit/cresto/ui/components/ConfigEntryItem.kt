package com.nevoit.cresto.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nevoit.cresto.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ConfigEntryItem(
    brush: Brush? = null,
    color: Color? = null,
    icon: Painter,
    title: String,
    isSwitch: Boolean? = false,
    enableGlow: Boolean? = false,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    var alphaValue by remember { mutableFloatStateOf(1f) }
    val animatedAlphaValue by animateFloatAsState(
        targetValue = alphaValue,
        animationSpec = tween(200)
    )
    val scope = rememberCoroutineScope()
    LaunchedEffect(isPressed) {
        alphaValue = if (isPressed) {
            0.5f
        } else {
            1f
        }
    }
    Row(
        modifier = Modifier
            .graphicsLayer(alpha = animatedAlphaValue)
            .clickable(
                enabled = true,
                onClick = {
                    scope.launch {
                        alphaValue = 0.5f
                        delay(400)
                        alphaValue = 1f
                    }
                    onClick()
                },
                interactionSource = interactionSource,
                indication = null
            )
            .fillMaxWidth()
            .height(40.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .then(
                    if (brush != null) Modifier.background(
                        brush = brush,
                        shape = CircleShape
                    ) else if (color != null) Modifier.background(
                        color = color,
                        shape = CircleShape
                    ) else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            if (enableGlow == true) {
                Icon(
                    painter = icon,
                    tint = Color.White.copy(.5f),
                    contentDescription = "$title config entry",
                    modifier = Modifier
                        .graphicsLayer(blendMode = BlendMode.Plus)
                        .fillMaxSize()
                        .blur(2.dp)
                )
            }
            Icon(
                painter = icon,
                tint = Color.White,
                contentDescription = "$title config entry",
                modifier = Modifier
                    .graphicsLayer(blendMode = BlendMode.Plus)
                    .fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(12.dp))
        if (isSwitch == null || !isSwitch) {
            Icon(
                painter = painterResource(R.drawable.ic_forward),
                tint = MaterialTheme.colorScheme.onBackground.copy(.2f),
                contentDescription = "Enter icon",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .height(40.dp)
                    .width(20.dp)
            )
        }
    }
}