package com.nevoit.cresto.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.nevoit.cresto.R
import com.nevoit.cresto.ui.theme.glasense.CalculatedColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun AnimatedCheckmarkIcon(
    @DrawableRes avdResId: Int,
    modifier: Modifier = Modifier
) {
    val avd = AnimatedImageVector.animatedVectorResource(id = avdResId)

    var atEnd by remember { mutableStateOf(false) }
    val painter = rememberAnimatedVectorPainter(animatedImageVector = avd, atEnd = atEnd)

    LaunchedEffect(Unit) {
        atEnd = true
    }

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier
    )
}

// !!!Needs refactor.
@Composable
fun CustomCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val defaultSize = with(LocalDensity.current) { 11.dp.toPx() }
    val defaultStroke = with(LocalDensity.current) { 2.dp.toPx() }

    val checkedSize = with(LocalDensity.current) { 12.dp.toPx() }
    val uncheckedSize = with(LocalDensity.current) { 6.dp.toPx() }

    val size = remember { Animatable(if (checked) checkedSize else uncheckedSize) }
    val alpha = remember { Animatable(if (checked) 1f else 0f) }

    val primary = MaterialTheme.colorScheme.primary
    val stroke = CalculatedColor.onSurfaceContainerBold

    val interactionSource = remember { MutableInteractionSource() }
    var animationToPlay by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(checked) {
        if (checked) {
            launch {
                size.animateTo(
                    targetValue = checkedSize,
                    animationSpec = tween(
                        durationMillis = 200,
                        easing = CubicBezierEasing(0.2f, 0.81f, 0.34f, 1.0f)
                    )
                )
            }
            launch {
                alpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 100)
                )
            }
        } else {
            launch {
                alpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 150)
                )
            }
            size.snapTo(checkedSize)
            kotlinx.coroutines.delay(150)
            size.snapTo(uncheckedSize)
        }
    }



    Box(
        modifier = modifier
            .drawBehind() {
                drawCircle(color = primary, radius = size.value, alpha = alpha.value)
                drawCircle(
                    color = stroke,
                    radius = defaultSize,
                    alpha = (1f - alpha.value),
                    style = Stroke(width = defaultStroke)
                )
            }
            .size(24.dp)
            .toggleable(
                value = checked,
                onValueChange = { newCheckedState ->
                    animationToPlay = if (newCheckedState) {
                        R.drawable.avd_checkmark_check
                    } else {
                        R.drawable.avd_checkmark_uncheck
                    }
                    onCheckedChange(newCheckedState)
                },
                enabled = true,
                role = Role.Checkbox,
                interactionSource = interactionSource,
                indication = null
            ),
        contentAlignment = Alignment.Center
    ) {

        if (animationToPlay != null) {
            key(animationToPlay) {
                AnimatedCheckmarkIcon(avdResId = animationToPlay!!)
            }
        } else {
            if (checked) {
                Image(
                    modifier = Modifier.scale(1.1f),
                    painter = painterResource(id = R.drawable.ic_checkbox_checkmark_animation_ready),
                    contentDescription = null
                )
            }
        }
    }
}