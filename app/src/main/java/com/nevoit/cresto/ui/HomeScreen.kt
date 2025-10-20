package com.nevoit.cresto.ui

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kyant.capsule.ContinuousCapsule
import com.kyant.capsule.ContinuousRoundedRectangle
import com.nevoit.cresto.CrestoApplication
import com.nevoit.cresto.R
import com.nevoit.cresto.data.TodoItem
import com.nevoit.cresto.ui.components.HorizontalFlagPicker
import com.nevoit.cresto.ui.components.HorizontalPresetDatePicker
import com.nevoit.cresto.ui.components.NAnimatedVisibility
import com.nevoit.cresto.ui.components.SwipeableTodoItem
import com.nevoit.cresto.ui.components.myFadeIn
import com.nevoit.cresto.ui.components.myFadeOut
import com.nevoit.cresto.ui.components.myScaleIn
import com.nevoit.cresto.ui.components.myScaleOut
import com.nevoit.cresto.ui.theme.glasense.AppButtonColors
import com.nevoit.cresto.ui.theme.glasense.CalculatedColor
import com.nevoit.cresto.ui.theme.glasense.getFlagColor
import com.nevoit.cresto.ui.theme.glasense.linearGradientMaskT2B70
import com.nevoit.cresto.util.deviceCornerShape
import com.nevoit.cresto.util.g2
import com.nevoit.cresto.util.getStatusBarHeight
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeApi::class)
@Composable
fun HomeScreen() {
    val scope = rememberCoroutineScope()

    val application = LocalContext.current.applicationContext as CrestoApplication
    val viewModel: TodoViewModel = viewModel(
        factory = TodoViewModelFactory(application.repository)
    )

    val todoList by viewModel.allTodos.collectAsStateWithLifecycle()

    var showSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val statusBarHeight = getStatusBarHeight()

    val thresholdPx = with(LocalDensity.current) { (statusBarHeight + 24.dp).toPx() }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                viewModel.totalScrollPx -= consumed.y
                return Offset.Zero
            }
        }
    }

    val isSmallTitleVisible by remember {
        derivedStateOf { viewModel.totalScrollPx > thresholdPx }
    }

    val hazeState = rememberHazeState()

    val colorMode = if (MaterialTheme.colorScheme.background == Color.White) 1 else 0

    val onSurfaceContainer = CalculatedColor.onSurfaceContainer

    val surfaceColor = CalculatedColor.hierarchicalBackgroundColor

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CalculatedColor.hierarchicalBackgroundColor)
            .nestedScroll(nestedScrollConnection)

    ) {
        LazyColumn(
            modifier = Modifier
                .hazeSource(hazeState, 0f)
                .fillMaxSize()
                .padding(0.dp)
                .background(CalculatedColor.hierarchicalBackgroundColor),
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 0.dp,
                end = 12.dp,
                bottom = 136.dp
            )
        ) {
            item {
                // Header
                Box(
                    modifier = Modifier
                        .padding(top = statusBarHeight)
                        .height(128.dp + statusBarHeight)
                        .fillMaxWidth()
                ) {
                    Text(
                        "All Todos",
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier
                            .padding(start = 12.dp, bottom = 16.dp)
                            .align(Alignment.BottomStart)
                    )
                }
            }

            items(items = todoList, key = { it.id }) { item ->
                SwipeableTodoItem(
                    item = item,
                    onCheckedChange = { isChecked ->
                        viewModel.update(item.copy(isCompleted = isChecked))
                    },
                    onDeleteClick = {
                        viewModel.delete(item)
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
        Box(
            modifier = Modifier
                .height(48.dp + statusBarHeight + 48.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter)


        ) {
            NAnimatedVisibility(
                visible = isSmallTitleVisible,
                enter = myFadeIn(),
                exit = myFadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .height(48.dp + statusBarHeight + 48.dp)
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .hazeEffect(hazeState) {
                            blurRadius = 2.dp
                            noiseFactor = 0f
                            inputScale = HazeInputScale.Fixed(0.5f)
                            mask = linearGradientMaskT2B70

                        }
                        .then(
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Modifier.smoothGradientMask(
                                surfaceColor.copy(alpha = 1f),
                                surfaceColor.copy(alpha = 0f),
                                0.5f,
                                0.5f,
                                0.7f
                            ) else Modifier.smoothGradientMaskFallbackInvert(surfaceColor, 0.7f)
                        )


                ) {}
            }


            Row(
                modifier = Modifier
                    .padding(top = statusBarHeight, start = 12.dp)
                    .height(48.dp)
                    .background(
                        onSurfaceContainer,
                        shape = ContinuousCapsule
                    ),
            ) {
                Box(
                    modifier = Modifier
                        .height(48.dp)
                        .width(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_magnifying_glass),
                        contentDescription = "Due Date",
                        modifier = Modifier.width(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Box(
                    modifier = Modifier
                        .height(48.dp)
                        .width(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_funnel),
                        contentDescription = "Due Date",
                        modifier = Modifier.width(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Box(
                modifier = Modifier
                    .padding(top = statusBarHeight, end = 12.dp)
                    .clip(CircleShape)
                    .height(48.dp)
                    .width(48.dp)
                    .background(
                        onSurfaceContainer,
                        shape = CircleShape
                    )
                    .align(Alignment.TopEnd)
                    .clickable() {
                        showSheet = true
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_large),
                    contentDescription = "Due Date",
                    modifier = Modifier.width(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            NAnimatedVisibility(
                visible = isSmallTitleVisible,
                enter = myScaleIn(
                    tween(200, 0, CubicBezierEasing(0.2f, 0.2f, 0f, 1f)),
                    0.9f
                ) + myFadeIn(tween(100)),
                exit = myScaleOut(
                    tween(200, 0, CubicBezierEasing(0.2f, 0.2f, 0f, 1f)),
                    0.9f
                ) + myFadeOut(tween(200)),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = statusBarHeight, bottom = 48.dp)
            ) {
                Text("All Todos", style = MaterialTheme.typography.headlineSmall)
            }
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            shape = deviceCornerShape(bottomLeft = false, bottomRight = false),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            AddTodoSheet(
                onAddClick = { title, flagIndex, finalDate ->
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showSheet = false
                        }
                        viewModel.insert(
                            TodoItem(
                                title = title,
                                flag = flagIndex,
                                dueDate = finalDate
                            )
                        )
                    }
                },
            )
        }
    }
}


enum class SelectedButton {
    DUE_DATE, FLAG, HASHTAG, NONE
}

@Composable
fun AddTodoSheet(
    onAddClick: (String, Int, LocalDate?) -> Unit
) {
    var selectedButton by remember { mutableStateOf(SelectedButton.NONE) }
    val state = rememberTextFieldState()
    val focusRequester = remember { FocusRequester() }
    var selectedIndex by remember { mutableIntStateOf(0) }
    var finalDate by remember { mutableStateOf<LocalDate?>(null) }
    val onAdd = {
        val text = state.text as String
        val dueDate = LocalDate.now()
        if (text.isNotBlank()) {
            onAddClick(text, selectedIndex, finalDate)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 0.dp, 12.dp, 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "New Task",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp)
        )


        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .height(48.dp)
                .background(
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05F),
                    ContinuousRoundedRectangle(12.dp)
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier.padding(16.dp, 0.dp)
            ) {
                BasicTextField(
                    state = state,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    onKeyboardAction = { onAdd() },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                )
            }

        }
        Spacer(modifier = Modifier.height(12.dp))
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            val totalWidth = this.maxWidth

            val collapsedSize = 48.dp
            val spacerSize = 12.dp
            val expandedWidth = totalWidth - (collapsedSize * 1) - 48.dp - (spacerSize * 2)
            val defaultWidth = (totalWidth - 48.dp - (spacerSize * 2)) / 2

            val dueDateWidth by animateDpAsState(
                targetValue = when (selectedButton) {
                    SelectedButton.DUE_DATE -> expandedWidth
                    SelectedButton.NONE -> defaultWidth
                    else -> collapsedSize
                },
                animationSpec = spring(
                    dampingRatio = 0.7f,
                    stiffness = 300f
                )
            )
            val flagWidth by animateDpAsState(
                targetValue = when (selectedButton) {
                    SelectedButton.FLAG -> expandedWidth
                    SelectedButton.NONE -> defaultWidth
                    else -> collapsedSize
                },
                animationSpec = spring(
                    dampingRatio = 0.7f,
                    stiffness = 300f
                )
            )
            val hashtagWidth by animateDpAsState(
                targetValue = when (selectedButton) {
                    SelectedButton.HASHTAG -> expandedWidth
                    SelectedButton.NONE -> defaultWidth
                    else -> collapsedSize
                },
                animationSpec = spring(
                    dampingRatio = 0.7f,
                    stiffness = 300f
                )
            )
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .width(totalWidth - 48.dp - spacerSize)
                        .height(48.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        enabled = true,
                        shape = ContinuousCapsule(g2),
                        onClick = {
                            selectedButton = if (selectedButton == SelectedButton.DUE_DATE) {
                                SelectedButton.NONE
                            } else {
                                SelectedButton.DUE_DATE
                            }
                        },
                        modifier = Modifier
                            .height(48.dp)
                            .width(dueDateWidth),
                        colors = AppButtonColors.secondary(),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            with(this@Button) {
                                AnimatedVisibility(
                                    visible = selectedButton != SelectedButton.DUE_DATE,
                                    enter = fadeIn(animationSpec = tween(delayMillis = 100)) + scaleIn(
                                        animationSpec = tween(delayMillis = 100),
                                        initialScale = 0.9f
                                    ),
                                    exit = fadeOut(animationSpec = tween(durationMillis = 100)) + scaleOut(
                                        animationSpec = tween(delayMillis = 100),
                                        targetScale = 0.9f
                                    )
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_calendar),
                                        contentDescription = "Due Date",
                                        modifier = Modifier.width(28.dp)
                                    )
                                }
                                AnimatedVisibility(
                                    visible = selectedButton == SelectedButton.DUE_DATE,
                                    enter = fadeIn(animationSpec = tween(delayMillis = 100)) + scaleIn(
                                        animationSpec = tween(delayMillis = 100),
                                        initialScale = 0.9f
                                    ),
                                    exit = fadeOut(animationSpec = tween(durationMillis = 100)) + scaleOut(
                                        animationSpec = tween(delayMillis = 100),
                                        targetScale = 0.9f
                                    )
                                ) {
                                    HorizontalPresetDatePicker(
                                        initialDate = finalDate,
                                        onDateSelected = {
                                            finalDate = it
                                            selectedButton = SelectedButton.NONE
                                        }
                                    )
                                }
                            }
                        }

                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        enabled = true,
                        shape = ContinuousCapsule(g2),
                        onClick = {
                            selectedButton = if (selectedButton == SelectedButton.FLAG) {
                                SelectedButton.NONE
                            } else {
                                SelectedButton.FLAG
                            }
                        },
                        modifier = Modifier
                            .height(48.dp)
                            .width(flagWidth),
                        colors = AppButtonColors.secondary(),
                        contentPadding = PaddingValues(0.dp),
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            with(this@Button) {
                                AnimatedVisibility(
                                    visible = selectedButton != SelectedButton.FLAG,
                                    enter = fadeIn(animationSpec = tween(delayMillis = 100)) + scaleIn(
                                        animationSpec = tween(delayMillis = 100),
                                        initialScale = 0.9f
                                    ),
                                    exit = fadeOut(animationSpec = tween(durationMillis = 100)) + scaleOut(
                                        animationSpec = tween(delayMillis = 100),
                                        targetScale = 0.9f
                                    )
                                ) {
                                    val displayColor = getFlagColor(selectedIndex)
                                    Icon(
                                        painter = if (displayColor == Color.Transparent) {
                                            painterResource(id = R.drawable.ic_flag)
                                        } else {
                                            painterResource(id = R.drawable.ic_flag_fill)
                                        },
                                        contentDescription = "Flag",
                                        modifier = Modifier.width(28.dp),
                                        tint = if (displayColor == Color.Transparent) {
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5F)
                                        } else {
                                            displayColor
                                        }
                                    )
                                }
                                AnimatedVisibility(
                                    visible = selectedButton == SelectedButton.FLAG,
                                    enter = fadeIn(animationSpec = tween(delayMillis = 100)) + scaleIn(
                                        animationSpec = tween(delayMillis = 100),
                                        initialScale = 0.9f
                                    ),
                                    exit = fadeOut(animationSpec = tween(durationMillis = 100)) + scaleOut(
                                        animationSpec = tween(delayMillis = 100),
                                        targetScale = 0.9f
                                    )
                                ) {

                                    HorizontalFlagPicker(
                                        selectedIndex = selectedIndex,
                                        onIndexSelected = { newIndex ->
                                            selectedIndex = newIndex
                                            selectedButton = SelectedButton.NONE
                                        }
                                    )

                                }
                            }
                        }

                    }
                    /*pacer(modifier = Modifier.width(12.dp))
                    Button(
                        enabled = true,
                        shape = ContinuousCapsule(g2),
                        onClick = {
                            selectedButton = if (selectedButton == SelectedButton.HASHTAG) {
                                SelectedButton.NONE
                            } else {
                                SelectedButton.HASHTAG
                            }
                        },
                        modifier = Modifier
                            .height(48.dp)
                            .width(hashtagWidth),
                        colors = AppButtonColors.secondary(),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_hashtag),
                            contentDescription = "Tag",
                            modifier = Modifier.width(28.dp)
                        )
                    }*/

                }
                Button(
                    enabled = true,
                    shape = CircleShape,
                    onClick = onAdd,
                    modifier = Modifier
                        .width(48.dp)
                        .height(48.dp),
                    colors = AppButtonColors.primary(),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .drawBehind {
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
                            }, contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_checkmark),
                            contentDescription = "Done",
                            modifier = Modifier.width(28.dp)
                        )
                    }

                }
            }

        }

    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}


private fun Modifier.smoothGradientMaskFallbackInvert(color: Color, alpha: Float): Modifier {
    return this.background(
        verticalGradient(
            colorStops = arrayOf(
                0.0f to color.copy(alpha = 1f),

                0.0361f to color.copy(alpha = 0.9f),
                0.0811f to color.copy(alpha = 0.8f),
                0.1343f to color.copy(alpha = 0.7f),
                0.1953f to color.copy(alpha = 0.6f),
                0.2637f to color.copy(alpha = 0.5f),
                0.3387f to color.copy(alpha = 0.42f),
                0.4201f to color.copy(alpha = 0.33f),
                0.5072f to color.copy(alpha = 0.25f),
                0.5995f to color.copy(alpha = 0.2f),
                0.6966f to color.copy(alpha = 0.15f),
                0.7979f to color.copy(alpha = 0.1f),
                0.903f to color.copy(alpha = 0.05f),
                1.0f to color.copy(alpha = 0.00f)
            )
        ), alpha = alpha
    )
}


