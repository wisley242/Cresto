package com.nevoit.cresto.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.capsule.ContinuousRoundedRectangle
import com.nevoit.cresto.R
import com.nevoit.cresto.data.TodoItem
import com.nevoit.cresto.ui.theme.glasense.CalculatedColor
import com.nevoit.cresto.ui.theme.glasense.Red500
import com.nevoit.cresto.ui.theme.glasense.getFlagColor
import com.nevoit.cresto.util.g2
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@Composable
fun TodoItemRow(
    item: TodoItem,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .defaultMinSize(minHeight = 68.dp)
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
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 12.dp)
        ) {
            Text(
                text = item.title,
                textDecoration = if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                style = MaterialTheme.typography.bodyMedium
            )
            if (item.dueDate != null) {
                Text(
                    text = item.dueDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp,
                    modifier = Modifier.alpha(0.4f)
                )
            }
        }

        if (getFlagColor(item.flag) != Color.Transparent) {
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_flag_fill),
                    contentDescription = "Flag",
                    modifier = Modifier.fillMaxSize(),
                    tint = getFlagColor(item.flag)
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
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
    IDLE,
    REVEALED
}

/* @Composable
fun SwipeableTodoItem(
    item: TodoItem,
    swipeState: SwipeState,
    offsetX: Float,
    onCheckedChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit,
    onDragStart: () -> Unit,
    onHorizontalDrag: (dragAmount: Float) -> Unit,
    onDragStopped: (velocity: Float) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val coroutineScope = rememberCoroutineScope()
    val screenWidth = LocalWindowInfo.current.containerSize.width

    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = spring(
            dampingRatio = 0.8f,
            stiffness = 500f
        )
    )

    val deleteSlideOffsetX = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }
    val alpha = remember { Animatable(1f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
                .width(72.dp),
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
                exit = myScaleOut(
                    tween(200, 0, LinearOutSlowInEasing),
                    0.6f
                ) + myFadeOut(tween(100))
            ) {
                Box(
                    modifier = Modifier
                        .graphicsLayer(
                            scaleX = scale.value,
                            scaleY = scale.value,
                            alpha = alpha.value
                        )
                        .clip(CircleShape)
                        .width(48.dp)
                        .height(48.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            enabled = true,
                            onClick = {
                                coroutineScope.launch {
                                    val jobs = listOf(
                                        launch {
                                            scale.animateTo(0.8f, tween(100))
                                        },
                                        launch { alpha.animateTo(0f, tween(100)) },
                                        launch {
                                            deleteSlideOffsetX.animateTo(
                                                targetValue = -screenWidth.toFloat() - offsetX,
                                                animationSpec = tween(
                                                    durationMillis = 100,
                                                    easing = CubicBezierEasing(
                                                        0.2f,
                                                        0f,
                                                        0.56f,
                                                        0.48f
                                                    )
                                                )
                                            )
                                        }
                                    )
                                    jobs.joinAll()
                                    onDeleteClick()
                                }
                            }
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
                .graphicsLayer { translationX = animatedOffsetX + deleteSlideOffsetX.value }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        onHorizontalDrag(delta)
                    },
                    onDragStarted = { onDragStart() },
                    onDragStopped = { velocity ->
                        onDragStopped(velocity)
                    }
                )
        ) {
            TodoItemRow(item, onCheckedChange)
        }
    }
}

@Composable
fun SwipeableTodoItemWithState(
    item: TodoItem,
    isRevealed: Boolean,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit
) {
    var swipeState by remember { mutableStateOf(SwipeState.IDLE) }
    var offsetX by remember { mutableFloatStateOf(0f) }

    // var offsetX by remember { mutableFloatStateOf(0f) }
    var initialSwipeState by remember { mutableStateOf(SwipeState.IDLE) }

    val coroutineScope = rememberCoroutineScope()

    val revealButtonWidthPx = with(LocalDensity.current) { 72.dp.toPx() }
    // val deleteThresholdPx = revealButtonWidthPx * 2
    val swipeThresholdPx = with(LocalDensity.current) { -60.dp.toPx() }

    val deleteDistanceThresholdPx = revealButtonWidthPx * 2

    val velocityThreshold = with(LocalDensity.current) { 400.dp.toPx() }
    val screenWidthPx = LocalWindowInfo.current.containerSize.width

    LaunchedEffect(isRevealed) {
        if (!isRevealed && offsetX != 0f) {
            offsetX = 0f
            swipeState = SwipeState.IDLE
        }
    }

    SwipeableTodoItem(
        item = item,
        swipeState = swipeState,
        offsetX = offsetX,
        onCheckedChange = onCheckedChange,
        onDeleteClick = onDeleteClick,
        onDragStart = {
            initialSwipeState = swipeState
            if (swipeState == SwipeState.IDLE) {
                onExpand()
            }
        },
        onHorizontalDrag = { dragAmount ->
            coroutineScope.launch {
                val newOffsetX = offsetX + dragAmount
                if (newOffsetX < 0) {
                    offsetX = newOffsetX
                }
            }
            swipeState = if (offsetX < swipeThresholdPx) {
                SwipeState.REVEALED
            } else {
                SwipeState.IDLE
            }
        },
        onDragStopped = { velocity ->
            coroutineScope.launch {
                if (initialSwipeState == SwipeState.REVEALED &&
                    (offsetX < -deleteDistanceThresholdPx || velocity < -velocityThreshold)
                ) {
                    offsetX = -screenWidthPx - revealButtonWidthPx
                    onDeleteClick()
                } else if (offsetX < -revealButtonWidthPx / 2) {
                    swipeState = SwipeState.REVEALED
                    offsetX = -revealButtonWidthPx
                    onExpand()
                } else {
                    offsetX = 0f
                    swipeState = SwipeState.IDLE
                    onCollapse()
                }
            }
        }
    )
} */

@Composable
fun SwipeableTodoItem(
    item: TodoItem,
    isRevealed: Boolean,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit
) {
    var swipeState by remember { mutableStateOf(SwipeState.IDLE) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var initialSwipeState by remember { mutableStateOf(SwipeState.IDLE) }
    val coroutineScope = rememberCoroutineScope()

    val density = LocalDensity.current
    val revealButtonWidthPx = with(density) { 72.dp.toPx() }
    val swipeThresholdPx = with(density) { -60.dp.toPx() }
    val deleteDistanceThresholdPx = revealButtonWidthPx * 2
    val velocityThreshold = with(density) { 400.dp.toPx() }
    var screenWidthPx by remember { mutableFloatStateOf(0f) }

    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 500f)
    )
    val deleteSlideOffsetX = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(isRevealed) {
        if (!isRevealed && offsetX != 0f) {
            coroutineScope.launch {
                offsetX = 0f
                swipeState = SwipeState.IDLE
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged { screenWidthPx = it.width.toFloat() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
                .width(72.dp),
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
                exit = myScaleOut(
                    tween(200, 0, LinearOutSlowInEasing),
                    0.6f
                ) + myFadeOut(tween(100))
            ) {
                Box(
                    modifier = Modifier
                        .graphicsLayer(
                            scaleX = scale.value,
                            scaleY = scale.value,
                            alpha = alpha.value
                        )
                        .clip(CircleShape)
                        .size(48.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                coroutineScope.launch {
                                    val jobs = listOf(
                                        launch { scale.animateTo(0.8f, tween(100)) },
                                        launch { alpha.animateTo(0f, tween(100)) },
                                        launch {
                                            deleteSlideOffsetX.animateTo(
                                                targetValue = -screenWidthPx - offsetX,
                                                animationSpec = tween(
                                                    100,
                                                    easing = CubicBezierEasing(
                                                        0.2f,
                                                        0f,
                                                        0.56f,
                                                        0.48f
                                                    )
                                                )
                                            )
                                        }
                                    )
                                    jobs.joinAll()
                                    onDeleteClick()
                                }
                            }
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
                .graphicsLayer { translationX = animatedOffsetX + deleteSlideOffsetX.value }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        val newOffsetX = (offsetX + delta).coerceAtMost(0f)
                        offsetX = newOffsetX
                        swipeState =
                            if (newOffsetX < swipeThresholdPx) SwipeState.REVEALED else SwipeState.IDLE
                    },
                    onDragStarted = {
                        initialSwipeState = swipeState
                        if (swipeState == SwipeState.IDLE) {
                            onExpand()
                        }
                    },
                    onDragStopped = { velocity ->
                        coroutineScope.launch {
                            if (initialSwipeState == SwipeState.REVEALED && (offsetX < -deleteDistanceThresholdPx || velocity < -velocityThreshold)) {
                                offsetX = -screenWidthPx - revealButtonWidthPx
                                onDeleteClick()
                            } else if (offsetX < -revealButtonWidthPx / 2) {
                                swipeState = SwipeState.REVEALED
                                offsetX = -revealButtonWidthPx
                                onExpand()
                            } else {
                                offsetX = 0f
                                swipeState = SwipeState.IDLE
                                onCollapse()
                            }
                        }
                    }
                )
        ) {
            TodoItemRow(item, onCheckedChange)
        }
    }
}