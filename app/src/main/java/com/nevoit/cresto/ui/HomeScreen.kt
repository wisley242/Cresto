package com.nevoit.cresto.ui

import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kyant.capsule.ContinuousCapsule
import com.nevoit.cresto.CrestoApplication
import com.nevoit.cresto.R
import com.nevoit.cresto.data.TodoItem
import com.nevoit.cresto.ui.components.AddTodoSheet
import com.nevoit.cresto.ui.components.DynamicSmallTitle
import com.nevoit.cresto.ui.components.PageHeader
import com.nevoit.cresto.ui.components.SwipeableTodoItem
import com.nevoit.cresto.ui.theme.glasense.CalculatedColor
import com.nevoit.cresto.util.deviceCornerShape
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeApi::class)
@Composable
fun HomeScreen() {
    val scope = rememberCoroutineScope()

    val application = LocalContext.current.applicationContext as CrestoApplication
    val viewModel: TodoViewModel = viewModel(
        factory = TodoViewModelFactory(application.repository)
    )

    val revealedItemId by viewModel.revealedItemId.collectAsState()

    val todoList by viewModel.allTodos.collectAsStateWithLifecycle()

    var showSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val density = LocalDensity.current
    val thresholdPx = if (statusBarHeight > 0.dp) {
        with(density) {
            (statusBarHeight + 24.dp).toPx()
        }
    } else 0f

    val hazeState = rememberHazeState()

    // val colorMode = if (MaterialTheme.colorScheme.background == Color.White) true else false

    val onSurfaceContainer = CalculatedColor.onSurfaceContainer

    val surfaceColor = CalculatedColor.hierarchicalBackgroundColor

    val lazyListState = rememberLazyListState()
    LaunchedEffect(lazyListState.isScrollInProgress, revealedItemId) {
        if (lazyListState.isScrollInProgress && revealedItemId != null) {
            viewModel.collapseRevealedItem()
        }
    }

    val isSmallTitleVisible by remember(thresholdPx) { derivedStateOf { ((lazyListState.firstVisibleItemIndex == 0) && (lazyListState.firstVisibleItemScrollOffset > thresholdPx)) || lazyListState.firstVisibleItemIndex > 0 } }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CalculatedColor.hierarchicalBackgroundColor)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        viewModel.collapseRevealedItem()
                    }
                )
            }
    ) {
        LazyColumn(
            state = lazyListState,
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
                PageHeader(title = "All Todos", statusBarHeight = statusBarHeight)
            }
            items(
                items = todoList,
                key = { it.id },
            ) { item ->
                Column(modifier = Modifier.animateItem(placementSpec = spring(0.9f, 400f))) {
                    SwipeableTodoItem(
                        item = item,
                        isRevealed = (item.id == revealedItemId),
                        onExpand = { viewModel.onItemExpanded(item.id) },
                        onCollapse = { viewModel.onItemCollapsed(item.id) },
                        onCheckedChange = { isChecked ->
                            viewModel.update(item.copy(isCompleted = isChecked))
                        },
                        onDeleteClick = { viewModel.delete(item) },
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

            }
        }
        DynamicSmallTitle(
            modifier = Modifier.align(Alignment.TopCenter),
            title = "All Todos",
            statusBarHeight = statusBarHeight,
            isVisible = isSmallTitleVisible,
            hazeState = hazeState,
            surfaceColor = surfaceColor
        ) {
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
                    .clickable {
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


