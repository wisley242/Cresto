package com.nevoit.cresto.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BottomSheet(
    containerColor: Color,
    onDismiss: () -> Unit,
    shape: Shape,
    onAddClick: (String, Int, LocalDate?) -> Unit
) {
    var columnHeightPx by remember { mutableIntStateOf(0) }
    var isVisible by remember { mutableStateOf(false) }
    val offset = remember { Animatable(Float.MAX_VALUE) }

    val scope = rememberCoroutineScope()
    var navigationBarHeight by remember { mutableIntStateOf(0) }

    val keyboardController = LocalSoftwareKeyboardController.current

    val isImeVisible = WindowInsets.isImeVisible

    LaunchedEffect(Unit) {
        isVisible = true
    }

    LaunchedEffect(columnHeightPx) {
        if (columnHeightPx > 0) {
            isVisible = true
            offset.snapTo(targetValue = (columnHeightPx + navigationBarHeight).toFloat())
            offset.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 200,
                    delayMillis = 100,
                    easing = CubicBezierEasing(.2f, .2f, 0f, 1f)
                )
            )
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        CustomAnimatedVisibility(
            visible = isVisible,
            enter = myFadeIn(tween(200)),
            exit = myFadeOut(tween(200))
        ) {
            Box(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        enabled = true,
                        onClick = {
                            keyboardController?.hide()
                            scope.launch {
                                isVisible = false
                                offset.animateTo(
                                    targetValue = (columnHeightPx + navigationBarHeight).toFloat(),
                                    animationSpec = tween(
                                        durationMillis = 200,
                                        delayMillis = if (isImeVisible) 100 else 0,
                                        easing = FastOutSlowInEasing
                                    )
                                )
                                onDismiss()
                            }
                        }
                    )
                    .background(Color.Black.copy(alpha = 0.4f))
                    .fillMaxSize()
            )
        }
        Column(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = true,
                    onClick = {}
                )
                .onKeyEvent { keyEvent ->
                    if (keyEvent.type == KeyEventType.KeyUp && (keyEvent.key == Key.Back || keyEvent.nativeKeyEvent.keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                        scope.launch {
                            isVisible = false
                            offset.animateTo(
                                targetValue = (columnHeightPx + navigationBarHeight).toFloat(),
                                animationSpec = tween(
                                    durationMillis = 200,
                                    easing = FastOutSlowInEasing
                                )
                            )
                            onDismiss()
                        }
                        true
                    } else {
                        false
                    }
                }
                .align(alignment = Alignment.BottomCenter)
                .graphicsLayer(translationY = offset.value)
        ) {
            Box(
                modifier = Modifier
                    .onSizeChanged { size ->
                        columnHeightPx = size.height
                    }
                    .background(
                        shape = shape,
                        color = containerColor
                    )
                    .fillMaxWidth()
            ) {
                AddTodoSheet(onAddClick = { title, flagIndex, finalDate ->
                    scope.launch {
                        isVisible = false
                        offset.animateTo(
                            targetValue = (columnHeightPx + navigationBarHeight).toFloat(),
                            animationSpec = tween(
                                durationMillis = 200,
                                delayMillis = 100,
                                easing = FastOutSlowInEasing
                            )
                        )
                        onAddClick(title, flagIndex, finalDate)
                        onDismiss()
                    }
                }, onClose = {
                    keyboardController?.hide()
                    scope.launch {
                        isVisible = false
                        offset.animateTo(
                            targetValue = (columnHeightPx + navigationBarHeight).toFloat(),
                            animationSpec = tween(
                                durationMillis = 200,
                                delayMillis = if (isImeVisible) 100 else 0,
                                easing = FastOutSlowInEasing
                            )
                        )
                        onDismiss()
                    }
                })
            }
            Box(
                modifier = Modifier
                    .onSizeChanged { size ->
                        navigationBarHeight = size.height
                    }
                    .background(color = containerColor)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .imePadding()
                    .fillMaxWidth()
            )
        }
    }
}