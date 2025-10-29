package com.nevoit.cresto.ui.menu

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.drawPlainBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.capsule.ContinuousRoundedRectangle
import com.nevoit.cresto.ui.components.ZeroHeightDivider
import com.nevoit.cresto.ui.theme.glasense.Red500
import com.nevoit.cresto.util.g2

@Composable
fun CustomMenuContent(items: List<MenuItemData>, onDismiss: () -> Unit) {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    val dividerColor = if (isSystemInDarkTheme) Color.White.copy(.1f) else Color.Black.copy(.1f)

    Column {
        items.forEachIndexed { index, item ->
            CustomMenuItem(
                text = item.text,
                icon = item.icon,
                isDestructive = item.isDestructive,
                onClick = {
                    onDismiss()
                    item.onClick()
                }
            )
            if (index < items.size - 1) {
                ZeroHeightDivider(
                    color = dividerColor,
                    width = 1.dp,
                    blendMode = BlendMode.Luminosity
                )
            }
        }
    }
}

@Composable
private fun CustomMenuItem(
    text: String,
    icon: Painter,
    isDestructive: Boolean,
    onClick: () -> Unit
) {
    val contentColor = if (isDestructive) Red500 else MaterialTheme.colorScheme.onBackground
    val interactionSource = remember { MutableInteractionSource() }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    alpha.animateTo(1f)
                }

                is PressInteraction.Release -> {
                    alpha.animateTo(0f)
                }

                is PressInteraction.Cancel -> {
                    alpha.animateTo(0f)
                }
            }
        }
    }
    Row(
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth()
            .drawBehind {
                drawRect(
                    color = Color.Black.copy(0.1f),
                    alpha = alpha.value
                )
            }
            .clickable(interactionSource = interactionSource, onClick = onClick, indication = null)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = 16.sp,
            lineHeight = 16.sp
        )
        Icon(
            painter = icon,
            contentDescription = text,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun GlasenseMenu(
    density: Density,
    menuState: MenuState,
    backdrop: LayerBackdrop,
    onDismiss: () -> Unit,
    modifier: Modifier,
    alphaAni: () -> Float,
    scaleAni: () -> Float
) {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    Box(
        modifier = Modifier
            .offset(
                x = with(density) { menuState.anchorPosition.x.toDp() },
                y = with(density) { menuState.anchorPosition.y.toDp() }
            )
            .zIndex(99f)
            .then(modifier)
            .dropShadow(
                RoundedCornerShape(16.dp),
                shadow = Shadow(
                    radius = 32.dp,
                    color = if (isSystemInDarkTheme) Color.Black.copy(alpha = 0.4f) else Color.Black.copy(
                        alpha = 0.1f
                    ),
                    offset = DpOffset(0.dp, 16.dp),
                    alpha = alphaAni()
                )
            )
            .drawPlainBackdrop(
                backdrop = backdrop,
                shape = { ContinuousRoundedRectangle(16.dp, g2) },
                layerBlock = {
                    alpha = alphaAni()
                },
                effects = {
                    blur(64f.dp.toPx(), TileMode.Mirror)
                },
                onDrawSurface = {
                    val outline = ContinuousRoundedRectangle(16.dp, g2).createOutline(
                        size = size,
                        layoutDirection = LayoutDirection.Ltr,
                        density = density
                    )
                    val gradientBrush = verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.White.copy(alpha = 1f),
                            1.0f to Color.White.copy(alpha = 0.2f)
                        )
                    )
                    if (!isSystemInDarkTheme) {
                        drawRect(
                            brush = SolidColor(Color(0xFF272727).copy(alpha = 0.2f)),
                            style = Fill,
                            blendMode = BlendMode.Luminosity,
                        )
                        drawRect(
                            brush = SolidColor(Color(0xFF252525).copy(alpha = 1f)),
                            style = Fill,
                            blendMode = BlendMode.Plus,
                        )
                        drawRect(
                            brush = SolidColor(Color(0xFF555555).copy(alpha = 0.5f)),
                            style = Fill,
                            blendMode = BlendMode.ColorDodge,
                        )
                        drawRect(
                            brush = SolidColor(Color(0xFFFFFFFF).copy(alpha = 0.2f)),
                            style = Fill,
                            blendMode = BlendMode.SrcOver,
                        )
                        drawOutline(
                            outline = outline,
                            brush = gradientBrush,
                            style = Stroke(width = 3.dp.toPx()),
                            blendMode = BlendMode.Plus,
                            alpha = 0.08f
                        )
                    } else {
                        drawRect(
                            brush = SolidColor(Color(0xFF000000).copy(alpha = 0.5f)),
                            style = Fill,
                            blendMode = BlendMode.Luminosity,
                        )
                        drawRect(
                            brush = SolidColor(Color(0xFF252525).copy(alpha = 1f)),
                            style = Fill,
                            blendMode = BlendMode.Plus,
                        )
                        drawRect(
                            brush = SolidColor(Color(0xFF4B4B4B).copy(alpha = 0.5f)),
                            style = Fill,
                            blendMode = BlendMode.ColorDodge,
                        )
                        drawRect(
                            brush = SolidColor(Color(0xFF000000).copy(alpha = 0.3f)),
                            style = Fill,
                            blendMode = BlendMode.SrcOver,
                        )
                        drawOutline(
                            outline = outline,
                            brush = gradientBrush,
                            style = Stroke(width = 3.dp.toPx()),
                            blendMode = BlendMode.Plus,
                            alpha = 0.08f
                        )
                    }
                }
            )
    ) {
        CustomMenuContent(items = menuState.items, onDismiss = onDismiss)
    }
}
