package com.nevoit.cresto.settings

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nevoit.cresto.R
import com.nevoit.cresto.ui.components.ConfigInfoHeader
import com.nevoit.cresto.ui.components.ConfigItem
import com.nevoit.cresto.ui.components.ConfigItemContainer
import com.nevoit.cresto.ui.components.DynamicSmallTitle
import com.nevoit.cresto.ui.theme.glasense.Blue500
import com.nevoit.cresto.ui.theme.glasense.CalculatedColor
import com.nevoit.cresto.ui.theme.glasense.Pink400
import com.nevoit.cresto.ui.theme.glasense.Purple500
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeApi::class)
@Composable
fun AIScreen() {
    val activity = LocalActivity.current

    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val density = LocalDensity.current
    val thresholdPx = if (statusBarHeight > 0.dp) {
        with(density) {
            (statusBarHeight + 24.dp).toPx()
        }
    } else 0f

    val hazeState = rememberHazeState()

    val onSurfaceContainer = CalculatedColor.onSurfaceContainer
    val onBackground = MaterialTheme.colorScheme.onBackground

    val surfaceColor = CalculatedColor.hierarchicalBackgroundColor
    val hierarchicalSurfaceColor = CalculatedColor.hierarchicalSurfaceColor

    val lazyListState = rememberLazyListState()

    val isSmallTitleVisible by remember(thresholdPx) { derivedStateOf { ((lazyListState.firstVisibleItemIndex == 0) && (lazyListState.firstVisibleItemScrollOffset > thresholdPx)) || lazyListState.firstVisibleItemIndex > 0 } }

    val dp = with(density) {
        1.dp.toPx()
    }

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
                Box(modifier = Modifier.padding(top = 48.dp + statusBarHeight + 12.dp))
            }
            item {
                ConfigInfoHeader(
                    brush = Brush.sweepGradient(
                        colorStops = arrayOf(
                            0f to Pink400,
                            0.33f to Purple500,
                            0.66f to Blue500,
                            1f to Pink400
                        )
                    ),
                    backgroundColor = hierarchicalSurfaceColor,
                    icon = painterResource(R.drawable.ic_twotone_sparkles),
                    title = "AI",
                    enableGlow = true,
                    info = "Boost your experience with intelligent Cresto function calling."
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                ConfigItemContainer(
                    title = "API",
                    backgroundColor = hierarchicalSurfaceColor
                ) {
                    Column {
                        ConfigItem(title = "API Key", isSwitch = false)
                        Spacer(modifier = Modifier.height(8.dp))
                        Spacer(
                            modifier = Modifier
                                .drawBehind {
                                    drawLine(
                                        color = onBackground.copy(.1f),
                                        start = Offset(x = 0f, y = 0f),
                                        end = Offset(this.size.width, y = 0f),
                                        strokeWidth = dp
                                    )
                                }
                                .fillMaxWidth()
                                .height(0.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        ConfigItem(title = "URL", isSwitch = false)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                ConfigItemContainer(
                    title = "Test",
                    backgroundColor = hierarchicalSurfaceColor
                ) {
                    Column() {
                        ConfigItem(title = "Input", isSwitch = false)
                        Spacer(modifier = Modifier.height(8.dp))
                        Spacer(
                            modifier = Modifier
                                .drawBehind {
                                    drawLine(
                                        color = onBackground.copy(.1f),
                                        start = Offset(x = 0f, y = 0f),
                                        end = Offset(this.size.width, y = 0f),
                                        strokeWidth = dp
                                    )
                                }
                                .fillMaxWidth()
                                .height(0.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        ConfigItem(title = "Response", isSwitch = false)
                    }
                }
                Spacer(modifier = Modifier.height(500.dp))
            }
        }
        DynamicSmallTitle(
            modifier = Modifier.align(Alignment.TopCenter),
            title = "AI",
            statusBarHeight = statusBarHeight,
            isVisible = isSmallTitleVisible,
            hazeState = hazeState,
            surfaceColor = surfaceColor
        ) {
        }
        Box(
            modifier = Modifier
                .padding(top = statusBarHeight, start = 12.dp)
                .clip(CircleShape)
                .height(48.dp)
                .width(48.dp)
                .background(
                    onSurfaceContainer,
                    shape = CircleShape
                )
                .align(Alignment.TopStart)
                .clickable {
                    activity?.finish()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_forward_nav),
                contentDescription = "Due Date",
                modifier = Modifier.width(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}