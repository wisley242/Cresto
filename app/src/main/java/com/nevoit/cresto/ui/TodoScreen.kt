package com.nevoit.cresto.ui

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kyant.capsule.ContinuousRoundedRectangle
import com.nevoit.cresto.R
import com.nevoit.cresto.ui.components.CustomNavigationButton
import com.nevoit.cresto.ui.gaussiangradient.smoothGradientMask
import com.nevoit.cresto.ui.gaussiangradient.smoothGradientMaskFallback
import com.nevoit.cresto.ui.menu.MenuItemData
import com.nevoit.cresto.ui.menu.MenuState
import com.nevoit.cresto.ui.theme.glasense.CalculatedColor
import com.nevoit.cresto.ui.theme.glasense.Red500
import com.nevoit.cresto.ui.theme.glasense.linearGradientMaskB2T70
import com.nevoit.cresto.ui.theme.glasense.linearGradientMaskB2T90
import com.nevoit.cresto.util.g2
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Star : Screen("star")
    object Settings : Screen("settings")
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeApi::class)
@Composable
fun TodoScreen() {
    val surfaceColor = CalculatedColor.hierarchicalBackgroundColor
    val navController = rememberNavController()
    val hazeState = rememberHazeState()

    var menuState by remember { mutableStateOf(MenuState()) }

    val showMenu: (anchorPosition: androidx.compose.ui.geometry.Offset, items: List<MenuItemData>) -> Unit =
        { position, items ->
            menuState = MenuState(isVisible = true, anchorPosition = position, items = items)
        }

    val dismissMenu = {
        menuState = menuState.copy(isVisible = false)
    }

    val density = LocalDensity.current
    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(hazeState, 0f)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            AppNavHost(
                navController = navController,
                showMenu = showMenu
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(136.dp)
                .align(Alignment.BottomCenter)
                .then(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Modifier.hazeEffect(
                        hazeState
                    ) {
                        blurRadius = 10.dp
                        progressive = HazeProgressive.verticalGradient(
                            startIntensity = 0.2f,
                            endIntensity = 0.6f
                        )
                        noiseFactor = 0f
                        mask = linearGradientMaskB2T90
                        inputScale = HazeInputScale.Fixed(0.5f)
                    } else Modifier.hazeEffect(
                        hazeState
                    ) {
                        blurRadius = 4.dp
                        noiseFactor = 0f
                        mask = linearGradientMaskB2T70
                    })

                .then(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Modifier.smoothGradientMask(
                        surfaceColor.copy(alpha = 0f), surfaceColor.copy(alpha = 1f), 0f, 1f, 0.6f
                    ) else Modifier.smoothGradientMaskFallback(surfaceColor, 0.6f)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
                    .height(56.dp)
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                CustomNavigationButton(
                    modifier = Modifier.weight(1f),
                    isActive = currentRoute == Screen.Home.route,
                    onClick = {
                        if (currentRoute != Screen.Home.route) {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }; launchSingleTop = true; restoreState = true
                            }
                        }
                    },
                    hazeState = hazeState
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_list),
                        contentDescription = "All To-Dos"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                CustomNavigationButton(
                    modifier = Modifier.weight(1f),
                    isActive = currentRoute == Screen.Star.route,
                    onClick = {
                        if (currentRoute != Screen.Star.route) {
                            navController.navigate(Screen.Star.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }; launchSingleTop = true; restoreState = true
                            }
                        }
                    },
                    hazeState = hazeState
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_star),
                        contentDescription = "Starred To-Dos"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                CustomNavigationButton(
                    modifier = Modifier.weight(1f),
                    isActive = currentRoute == Screen.Settings.route,
                    onClick = {
                        if (currentRoute != Screen.Settings.route) {
                            navController.navigate(Screen.Settings.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }; launchSingleTop = true; restoreState = true
                            }
                        }
                    },
                    hazeState = hazeState
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_gear),
                        contentDescription = "Settings"
                    )
                }
            }
        }
        if (menuState.isVisible) {
            Box(
                modifier = Modifier
                    .offset(
                        x = with(density) { menuState.anchorPosition.x.toDp() },
                        y = with(density) { menuState.anchorPosition.y.toDp() + 48.dp }
                    )
                    .zIndex(99f)
                    .dropShadow(
                        ContinuousRoundedRectangle(16.dp, g2),
                        shadow = Shadow(
                            radius = 64.dp,
                            color = Color.Black.copy(alpha = 0.1f),
                            offset = DpOffset(0.dp, 16.dp)
                        )
                    )
                    .clip(ContinuousRoundedRectangle(16.dp, g2))
                    .hazeEffect(hazeState) {
                        blurRadius = 64.dp
                        noiseFactor = 0f
                    }

                    .drawBehind {
                        val size = this.size
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
                        drawOutline(
                            outline = outline,
                            brush = gradientBrush,
                            style = Stroke(width = 3.dp.toPx()),
                            blendMode = BlendMode.Plus,
                            alpha = 0.08f
                        )
                    }
            ) {
                CustomMenuContent(items = menuState.items, onDismiss = dismissMenu)
            }
        }
    }
}

@Composable
private fun CustomMenuContent(items: List<MenuItemData>, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .width(200.dp)
    ) {
        Column() {
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
                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = Color.Gray.copy(alpha = 0.2f)
                    )
                }
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

    Row(
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
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
            modifier = Modifier.size(20.dp)
        )
    }
}
