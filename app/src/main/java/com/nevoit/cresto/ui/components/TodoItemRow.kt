package com.nevoit.cresto.ui.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousRoundedRectangle
import com.nevoit.cresto.R
import com.nevoit.cresto.data.TodoItem
import com.nevoit.cresto.ui.theme.glasense.CalculatedColor
import com.nevoit.cresto.ui.theme.glasense.Red500
import com.nevoit.cresto.ui.theme.glasense.getFlagColor
import com.nevoit.cresto.util.g2
import kotlinx.coroutines.launch

@Composable
fun TodoItemRow(
    item: TodoItem,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .defaultMinSize(minHeight = 72.dp)
            .fillMaxWidth()
            .background(
                color = CalculatedColor.hierarchicalSurfaceColor,
                shape = ContinuousRoundedRectangle(12.dp, g2),
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(12.dp))
        CustomCheckbox(
            checked = item.isCompleted,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = item.title,
            modifier = Modifier.weight(1f),
            textDecoration = if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None
        )
        Text(
            text = if (item.dueDate != null) item.dueDate.toString() else "",
            modifier = Modifier.weight(1f),
        )
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(getFlagColor(item.flag))
                .width(8.dp)
                .height(8.dp)
        )
        Box(
            modifier = Modifier
                .height(32.dp)
                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f))
        ) {
            item.hashtag?.let { Text(text = it, color = MaterialTheme.colorScheme.onBackground) }
        }
    }
}

enum class SwipeState {
    IDLE,       // 静止状态
    REVEALED    // “删除”按钮已显示
}

@Composable
fun SwipeableTodoItem(
    item: TodoItem,
    onCheckedChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var swipeState by remember { mutableStateOf(SwipeState.IDLE) }
    var offsetX by remember { mutableStateOf(0f) }

    // 定义阈值
    val revealButtonWidth = 80.dp
    val revealButtonWidthPx = with(LocalDensity.current) { revealButtonWidth.toPx() }
    val deleteThresholdPx = revealButtonWidthPx * 2 // 滑动多远直接删除

    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = spring(
            dampingRatio = 0.7f,
            stiffness = 400f
        )
    )

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
                .width(80.dp),
            contentAlignment = Alignment.Center
        ) {
            NAnimatedVisibility(
                visible = (swipeState == SwipeState.REVEALED),
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp),
                enter = myScaleIn(
                    tween(200, 0, LinearOutSlowInEasing),
                    0.6f
                ) + myFadeIn(tween(100)),
                exit = myScaleOut() + myFadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .width(48.dp)
                        .height(48.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            enabled = true,
                            onClick = onDeleteClick
                        )
                        .drawBehind() {
                            drawCircle(color = Red500, radius = this.size.width / 2)
                            val outline = CircleShape.createOutline(
                                size = this.size,
                                layoutDirection = this.layoutDirection,
                                density = this,
                            )
                            val gradientBrush = verticalGradient(
                                colorStops = arrayOf(
                                    0.0f to Color.White.copy(alpha = 0.2f),
                                    1.0f to Color.White.copy(alpha = 0.02f)
                                )
                            )
                            drawOutline(
                                outline = outline,
                                brush = gradientBrush,
                                style = Stroke(width = 3.dp.toPx()),
                                blendMode = BlendMode.Plus
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_trash),
                        contentDescription = "Delete Task",
                        tint = Color.White,
                        modifier = Modifier
                            .width(28.dp)
                            .height(28.dp)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .graphicsLayer(translationX = animatedOffsetX)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
                                if (offsetX < -deleteThresholdPx) {
                                    onDeleteClick()
                                } else if (offsetX < -revealButtonWidthPx / 2) {
                                    offsetX = -revealButtonWidthPx
                                    swipeState = SwipeState.REVEALED
                                } else {
                                    offsetX = 0f
                                    swipeState = SwipeState.IDLE
                                }
                            }
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            val newOffsetX = offsetX + dragAmount
                            if (newOffsetX < 0) {
                                offsetX = newOffsetX
                            }
                            change.consume()
                        }
                    )
                }
        ) {
            TodoItemRow(item, onCheckedChange)
        }
    }
}
