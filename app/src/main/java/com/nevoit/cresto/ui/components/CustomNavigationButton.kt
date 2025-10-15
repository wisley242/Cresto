package com.nevoit.cresto.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousCapsule
import com.nevoit.cresto.ui.theme.glasense.NavigationButtonActiveColors
import com.nevoit.cresto.ui.theme.glasense.NavigationButtonNormalColors
import com.nevoit.cresto.util.g2
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

@Composable
fun CustomNavigationButton(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    onClick: () -> Unit,
    hazeState: HazeState,
    content: @Composable () -> Unit
) {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    val finalModifier = if (isActive) {
        modifier
            .dropShadow(
                ContinuousCapsule,
                Shadow(
                    radius = 24.dp,
                    color = Color.Black.copy(alpha = 0.08f),
                    spread = 0.dp,
                    offset = DpOffset(0.dp, 8.dp)
                )
            )
            .fillMaxHeight()
    } else {
        modifier
            .dropShadow(
                ContinuousCapsule,
                Shadow(
                    radius = 24.dp,
                    color = Color.Black.copy(alpha = 0.08f),
                    spread = 0.dp,
                    offset = DpOffset(0.dp, 8.dp)
                )
            )
            .clip(ContinuousCapsule(g2))
            .hazeEffect(hazeState) {
                style = HazeStyle(
                    backgroundColor = Color.White.copy(alpha = 0f),
                    tint = null,
                    blurRadius = 32.dp,
                    noiseFactor = 0f,
                    fallbackTint = HazeTint.Unspecified
                )
            }
            .fillMaxHeight()

    }

    Button(
        shape = ContinuousCapsule(g2),
        onClick = onClick,
        modifier = finalModifier,
        colors = if (isActive) NavigationButtonActiveColors.primary() else NavigationButtonNormalColors.primary(),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Box(
                modifier = Modifier
                    .drawWithCache {
                        val outline = ContinuousCapsule(g2).createOutline(
                            size = this.size,
                            layoutDirection = this.layoutDirection,
                            density = this
                        )
                        val gradientBrush = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to Color.White.copy(alpha = 0.2f),
                                1.0f to Color.White.copy(alpha = 0.02f)
                            )
                        )
                        if (!isSystemInDarkTheme && !isActive) {
                            onDrawBehind {
                                drawOutline(
                                    outline = outline,
                                    brush = SolidColor(Color(0xFF888888).copy(alpha = 0.7f)),
                                    style = Fill,
                                    blendMode = BlendMode.Luminosity
                                )
                                drawOutline(
                                    outline = outline,
                                    brush = SolidColor(Color(0xFF5F5F5F).copy(alpha = 1f)),
                                    style = Fill,
                                    blendMode = BlendMode.ColorDodge
                                )
                                drawOutline(
                                    outline = outline,
                                    brush = SolidColor(Color(0xFF555555).copy(alpha = 0.5f)),
                                    style = Fill,
                                    blendMode = BlendMode.ColorDodge
                                )
                                drawOutline(
                                    outline = outline,
                                    brush = SolidColor(Color(0xFFFFFFFF).copy(alpha = 0.1f)),
                                    style = Fill,
                                    blendMode = BlendMode.SrcOver
                                )
                                drawOutline(
                                    outline = outline,
                                    brush = gradientBrush,
                                    style = Stroke(width = 3.dp.toPx()),
                                    blendMode = BlendMode.Plus
                                )
                            }

                        } else if (isSystemInDarkTheme && !isActive) {
                            onDrawBehind {
                                drawOutline(
                                    outline = outline,
                                    brush = SolidColor(Color(0xFFFFFFFF).copy(alpha = 0.1f)),
                                    style = Fill
                                )
                                drawOutline(
                                    outline = outline,
                                    brush = SolidColor(Color(0xFF555555).copy(alpha = 0.5f)),
                                    style = Fill,
                                    blendMode = BlendMode.ColorDodge
                                )
                                drawOutline(
                                    outline = outline,
                                    brush = SolidColor(Color(0xFF000000).copy(alpha = 0.2f)),
                                    style = Fill,
                                    blendMode = BlendMode.Luminosity
                                )
                                drawOutline(
                                    outline = outline,
                                    brush = SolidColor(Color(0xFF000000).copy(alpha = 0.2f)),
                                    style = Fill,
                                    blendMode = BlendMode.Overlay
                                )
                                drawOutline(
                                    outline = outline,
                                    brush = SolidColor(Color(0xFF595959).copy(alpha = 0.4f)),
                                    style = Fill,
                                    blendMode = BlendMode.Luminosity
                                )
                                drawOutline(
                                    outline = outline,
                                    brush = gradientBrush,
                                    style = Stroke(width = 3.dp.toPx()),
                                    blendMode = BlendMode.Plus
                                )
                            }
                        } else {
                            onDrawBehind {
                                drawOutline(
                                    outline = outline,
                                    brush = gradientBrush,
                                    style = Stroke(width = 3.dp.toPx()),
                                    blendMode = BlendMode.Plus
                                )
                            }
                        }
                    }
                    .fillMaxSize()
            )
            content()
        }
    }
}