package com.nevoit.cresto.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.nevoit.cresto.ui.theme.glasense.Blue500
import com.nevoit.cresto.ui.theme.glasense.Gray500
import com.nevoit.cresto.ui.theme.glasense.Green500
import com.nevoit.cresto.ui.theme.glasense.Orange500
import com.nevoit.cresto.ui.theme.glasense.Purple500
import com.nevoit.cresto.ui.theme.glasense.Red500
import com.nevoit.cresto.ui.theme.glasense.Yellow500
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HorizontalFlagPicker(
    selectedIndex: Int,
    onIndexSelected: (Int) -> Unit
) {
    val colors = listOf(
        Color.Transparent,
        Red500,
        Orange500,
        Yellow500,
        Green500,
        Blue500,
        Purple500,
        Gray500
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(14.dp)
    ) {
        itemsIndexed(colors) { index, color ->
            ColorCircle(
                color = color,
                isSelected = (selectedIndex == index),
                onClick = {
                    onIndexSelected(index)
                }
            )
        }
    }
}

@Composable
fun ColorCircle(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale = remember { Animatable(1f) }

    val interactionSource = remember { MutableInteractionSource() }

    var isPressed by remember { mutableStateOf(false) }

    LaunchedEffect(interactionSource) {
        // 监听交互事件流
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                // 当手指按下时
                is PressInteraction.Press -> {
                    isPressed = true
                    // 启动一个动画，将圆形放大
                    scale.animateTo(
                        targetValue = 1.4f,
                        animationSpec = tween(150)
                    )
                }
                // 当手指抬起或取消时
                is PressInteraction.Release -> {
                    isPressed = true
                    coroutineScope {
                        // 任务 A: 启动完整的恢复动画
                        launch {
                            scale.animateTo(
                                targetValue = 0.8f,
                                animationSpec = tween(300)
                            )
                        }

                        // 任务 B: 启动一个只负责延迟并调用 onClick 的任务
                        launch {
                            // 等待动画时长的一半
                            delay(100)
                            // 在中途调用 onClick
                            onClick()
                        }
                    }
                }

                is PressInteraction.Cancel -> {
                    isPressed = false
                    // 启动一个动画，将圆形恢复原状
                    scale.animateTo(
                        targetValue = 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .graphicsLayer {
                // 应用当前的缩放值
                scaleX = scale.value
                scaleY = scale.value
            }
            .size(20.dp)
            .background(color, CircleShape)
            .clip(CircleShape)
            .clickable(
                // 4. 将 interactionSource 传递给 clickable
                interactionSource = interactionSource,
                // 我们不再需要涟漪效果
                indication = null
            ) {

            }
            .then(
                // 选中状态的绘制逻辑保持不变
                if (isSelected || isPressed) {
                    Modifier.drawBehind {
                        drawCircle(
                            color = Color.Black.copy(alpha = 0.2f),
                            style = Stroke(width = 4.dp.toPx())
                        )
                    }
                } else {
                    Modifier
                }
            )
    )
}