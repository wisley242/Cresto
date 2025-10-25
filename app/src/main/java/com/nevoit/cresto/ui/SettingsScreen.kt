package com.nevoit.cresto.ui

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kyant.capsule.ContinuousCapsule
import com.kyant.capsule.ContinuousRoundedRectangle
import com.nevoit.cresto.CrestoApplication
import com.nevoit.cresto.R
import com.nevoit.cresto.settings.AIActivity
import com.nevoit.cresto.settings.AppearanceActivity
import com.nevoit.cresto.settings.DataStorageActivity
import com.nevoit.cresto.ui.components.ConfigContainer
import com.nevoit.cresto.ui.components.ConfigEntryItem
import com.nevoit.cresto.ui.components.DynamicSmallTitle
import com.nevoit.cresto.ui.components.PageHeader
import com.nevoit.cresto.ui.theme.glasense.AppButtonColors
import com.nevoit.cresto.ui.theme.glasense.Blue500
import com.nevoit.cresto.ui.theme.glasense.CalculatedColor
import com.nevoit.cresto.ui.theme.glasense.Emerald500
import com.nevoit.cresto.ui.theme.glasense.Pink400
import com.nevoit.cresto.ui.theme.glasense.Purple500
import com.nevoit.cresto.ui.theme.glasense.Slate500
import com.nevoit.cresto.ui.viewmodel.AiViewModel
import com.nevoit.cresto.ui.viewmodel.UiState
import com.nevoit.cresto.util.g2
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeApi::class)
@Composable
fun SettingsScreen(aiViewModel: AiViewModel = viewModel()) {
    val scope = rememberCoroutineScope()

    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val density = LocalDensity.current
    val thresholdPx = if (statusBarHeight > 0.dp) {
        with(density) {
            (statusBarHeight + 24.dp).toPx()
        }
    } else 0f

    val hazeState = rememberHazeState()

    val onSurfaceContainer = CalculatedColor.onSurfaceContainer

    val surfaceColor = CalculatedColor.hierarchicalBackgroundColor
    val hierarchicalSurfaceColor = CalculatedColor.hierarchicalSurfaceColor

    val lazyListState = rememberLazyListState()

    val isSmallTitleVisible by remember(thresholdPx) { derivedStateOf { ((lazyListState.firstVisibleItemIndex == 0) && (lazyListState.firstVisibleItemScrollOffset > thresholdPx)) || lazyListState.firstVisibleItemIndex > 0 } }

    var promptText by remember { mutableStateOf("") }
    var apiKey by remember { mutableStateOf("") }

    val uiState by aiViewModel.uiState.collectAsState()

    val application = LocalContext.current.applicationContext as CrestoApplication
    val viewModel: TodoViewModel = viewModel(
        factory = TodoViewModelFactory(application.repository)
    )

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
    ) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .hazeSource(hazeState, 0f)
                .fillMaxSize()
                .padding(0.dp)
                .background(surfaceColor),
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 0.dp,
                end = 12.dp,
                bottom = 136.dp
            )
        ) {
            item {
                PageHeader(title = "Settings", statusBarHeight = statusBarHeight)
            }
            item {
                ConfigContainer(backgroundColor = hierarchicalSurfaceColor) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        ConfigEntryItem(
                            brush = Brush.sweepGradient(
                                colorStops = arrayOf(
                                    0f to Pink400,
                                    0.33f to Purple500,
                                    0.66f to Blue500,
                                    1f to Pink400
                                )
                            ),
                            icon = painterResource(R.drawable.ic_twotone_sparkles),
                            title = "AI",
                            enableGlow = true,
                            onClick = {
                                val intent = Intent(context, AIActivity::class.java)
                                context.startActivity(intent)
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                ConfigContainer(backgroundColor = hierarchicalSurfaceColor) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        ConfigEntryItem(
                            color = Blue500,
                            icon = painterResource(R.drawable.ic_twotone_image),
                            title = "Appearance",
                            onClick = {
                                val intent = Intent(context, AppearanceActivity::class.java)
                                context.startActivity(intent)
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ConfigEntryItem(
                            color = Slate500,
                            icon = painterResource(R.drawable.ic_twotone_storage),
                            title = "Data & Storage",
                            onClick = {
                                val intent = Intent(context, DataStorageActivity::class.java)
                                context.startActivity(intent)
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ConfigEntryItem(
                            color = Slate500,
                            icon = painterResource(R.drawable.ic_twotone_gear),
                            title = "General",
                            onClick = {}
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                ConfigContainer(backgroundColor = hierarchicalSurfaceColor) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        color = Emerald500,
                                        shape = ContinuousRoundedRectangle(12.dp, g2)
                                    )
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Text(
                                    text = "Cresto",
                                    fontSize = 20.sp,
                                    lineHeight = 20.sp,
                                    fontWeight = FontWeight.W500,
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                                Text(
                                    text = "Developed by Nevoit",
                                    fontSize = 12.sp,
                                    lineHeight = 12.sp,
                                    fontWeight = FontWeight.W400,
                                    color = MaterialTheme.colorScheme.onBackground.copy(.5f),
                                )
                                Text(
                                    text = "Version 0.0.1",
                                    fontSize = 12.sp,
                                    lineHeight = 12.sp,
                                    fontWeight = FontWeight.W400,
                                    color = MaterialTheme.colorScheme.onBackground.copy(.5f),
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(
                                painter = painterResource(R.drawable.ic_forward),
                                tint = MaterialTheme.colorScheme.onBackground.copy(.2f),
                                contentDescription = "Enter icon",
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .height(40.dp)
                                    .width(20.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                ConfigContainer(backgroundColor = hierarchicalSurfaceColor) {
                    ConfigEntryItem(
                        color = Slate500,
                        icon = painterResource(R.drawable.ic_twotone_info),
                        title = "Credits",
                        onClick = {}
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                ConfigContainer(title = "Test Only", backgroundColor = hierarchicalSurfaceColor) {
                    Column {
                        OutlinedTextField(
                            value = apiKey,
                            onValueChange = { apiKey = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = ContinuousRoundedRectangle(12.dp, g2)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = promptText,
                                onValueChange = { promptText = it },
                                modifier = Modifier.weight(1f),
                                shape = ContinuousRoundedRectangle(12.dp, g2)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Button(
                                onClick = {
                                    aiViewModel.generateContent(promptText, apiKey)
                                },
                                shape = ContinuousCapsule,
                                modifier = Modifier.size(48.dp),
                                contentPadding = PaddingValues(0.dp),
                                colors = AppButtonColors.primary()

                            ) {
                                Text("Go")
                            }
                        }
                        when (val state = uiState) {
                            is UiState.Initial -> {
                                Text("Input text")
                            }

                            is UiState.Loading -> {
                                CircularProgressIndicator()
                            }

                            is UiState.Success -> {
                                Column {
                                    Text(
                                        "${state.response.quantity} tasks in total",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    viewModel.insertAiGeneratedTodos(state.response.items)
                                    aiViewModel.clearState()
                                }
                            }

                            is UiState.Error -> {
                                Text(
                                    text = "Error: ${state.message}",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }

                }
            }
            item {
                Spacer(Modifier.height(200.dp))
            }
        }
        DynamicSmallTitle(
            modifier = Modifier.align(Alignment.TopCenter),
            title = "Settings",
            statusBarHeight = statusBarHeight,
            isVisible = isSmallTitleVisible,
            hazeState = hazeState,
            surfaceColor = surfaceColor
        ) {
        }
    }

}
