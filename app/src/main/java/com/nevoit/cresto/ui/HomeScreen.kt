package com.nevoit.cresto.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kyant.capsule.ContinuousCapsule
import com.kyant.capsule.ContinuousRoundedRectangle
import com.nevoit.cresto.CrestoApplication
import com.nevoit.cresto.R
import com.nevoit.cresto.data.TodoItem
import com.nevoit.cresto.ui.components.HorizontalFlagPicker
import com.nevoit.cresto.ui.theme.glasense.AppButtonColors
import com.nevoit.cresto.ui.theme.glasense.getFlagColor
import com.nevoit.cresto.util.deviceCornerShape
import com.nevoit.cresto.util.g2
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    // 1. 获取 ViewModel
    val application = LocalContext.current.applicationContext as CrestoApplication
    val viewModel: TodoViewModel = viewModel(
        factory = TodoViewModelFactory(application.repository)
    )

    // 2. 订阅 ViewModel 中的状态
    val todoList by viewModel.allTodos.collectAsStateWithLifecycle()

    var showSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val scope = rememberCoroutineScope()

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = { Text("To-Do") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                shape = ContinuousRoundedRectangle(16.dp, g2),
                onClick = { showSheet = true },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(horizontal = 0.dp, vertical = 136.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp)
                    .background(color = MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(
                    start = 0.dp,
                    top = 0.dp,
                    end = 0.dp,
                    bottom = 136.dp
                )
            ) {
                items(items = todoList, key = { it.id }) { item ->
                    TodoItemRow(
                        item = item,
                        onCheckedChange = { isChecked ->
                            viewModel.update(item.copy(isCompleted = isChecked))
                        },
                        onDeleteClick = {
                            viewModel.delete(item)
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            shape = deviceCornerShape(bottomLeft = false, bottomRight = false)
        ) {
            AddTodoSheet(
                onAddClick = { title, flagIndex ->
                    // 使用协程来先关闭 sheet，再执行插入，体验更流畅
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        // 动画完成后，再彻底隐藏 Composable
                        if (!sheetState.isVisible) {
                            showSheet = false
                        }
                        // 最后，调用 viewModel 插入数据
                        viewModel.insert(TodoItem(title = title, flag = flagIndex))
                    }
                }
            )
        }
    }
}

@Composable
fun TodoItemRow(
    item: TodoItem,
    onCheckedChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isCompleted,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = item.title,
            modifier = Modifier.weight(1f),
            textDecoration = if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None
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
        IconButton(onClick = onDeleteClick) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Task"
            )
        }
    }
}


enum class SelectedButton {
    DUE_DATE, FLAG, HASHTAG, NONE
}

@Composable
fun AddTodoSheet(onAddClick: (String, Int) -> Unit) {
    var selectedButton by remember { mutableStateOf(SelectedButton.NONE) }
    val state = rememberTextFieldState()
    val focusRequester = remember { FocusRequester() }
    var selectedIndex by remember { mutableIntStateOf(0) }
    val onAdd = {
        val text = state.text as String
        if (text.isNotBlank()) {
            onAddClick(text, selectedIndex)
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
            textAlign = androidx.compose.ui.text.style.TextAlign.Start,
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

            // 2. 定义固定尺寸和计算展开后的尺寸
            val collapsedSize = 48.dp
            val spacerSize = 12.dp
            // 展开后的宽度 = 总宽度 - 2个收缩的按钮 - 1个固定的确认按钮 - 3个间隔
            val expandedWidth = totalWidth - (collapsedSize * 2) - 48.dp - (spacerSize * 3)
            // “无选中”状态下，每个按钮的平均宽度
            val defaultWidth = (totalWidth - 48.dp - (spacerSize * 3)) / 3

            // 3. 为每个按钮的宽度创建动画状态
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
                                SelectedButton.NONE // 如果再次点击自己，则恢复原样
                            } else {
                                SelectedButton.DUE_DATE // 否则就选中自己
                            }
                        },
                        modifier = Modifier
                            .height(48.dp)
                            .width(dueDateWidth),
                        colors = AppButtonColors.secondary(),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_calendar),
                            contentDescription = "Due Date",
                            modifier = Modifier.width(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        enabled = true,
                        shape = ContinuousCapsule(g2),
                        onClick = {
                            selectedButton = if (selectedButton == SelectedButton.FLAG) {
                                SelectedButton.NONE // 如果再次点击自己，则恢复原样
                            } else {
                                SelectedButton.FLAG // 否则就选中自己
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
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_flag),
                                        contentDescription = "Flag",
                                        modifier = Modifier.width(28.dp)
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
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        enabled = true,
                        shape = ContinuousCapsule(g2),
                        onClick = {
                            selectedButton = if (selectedButton == SelectedButton.HASHTAG) {
                                SelectedButton.NONE // 如果再次点击自己，则恢复原样
                            } else {
                                SelectedButton.HASHTAG // 否则就选中自己
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
                    }

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
                                val gradientBrush = Brush.verticalGradient(
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


