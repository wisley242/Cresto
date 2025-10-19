package com.nevoit.cresto.ui

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nevoit.cresto.R
import com.nevoit.cresto.ui.components.CustomNavigationButton
import com.nevoit.cresto.ui.gaussiangradient.GAUSSIAN_COLOR_INTERPOLATION_SHADER
import com.nevoit.cresto.ui.theme.glasense.CalculatedColor
import com.nevoit.cresto.ui.theme.glasense.linearGradientMaskB2T70
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Star : Screen("star")
    object Settings : Screen("settings")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen() {
    val surfaceColor = CalculatedColor.hierarchicalBackgroundColor
    val navController = rememberNavController()
    val hazeState = rememberHazeState()

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
                navController = navController
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
                        blurRadius = 6.dp
                        progressive = HazeProgressive.verticalGradient(
                            startIntensity = 0f,
                            endIntensity = 1f
                        )
                        noiseFactor = 0f
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
    }


}


@Composable
fun AppNavHost(
    navController: NavHostController
) {
    val fadeDuration = 0
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route // 设置起始页
    ) {
        composable(
            route = Screen.Home.route,
            enterTransition = {
                fadeIn(animationSpec = tween(fadeDuration))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(fadeDuration))
            }
        ) {
            HomeScreen()

        }

        composable(
            route = Screen.Star.route,
            enterTransition = {
                fadeIn(animationSpec = tween(fadeDuration))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(fadeDuration))
            }
        ) {
            StarScreen()

        }

        composable(
            route = Screen.Settings.route,
            enterTransition = {
                fadeIn(animationSpec = tween(fadeDuration))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(fadeDuration))
            }
        ) {
            // 这里是您个人资料页的 UI

        }
    }
}

@Composable
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun Modifier.smoothGradientMask(
    startColor: Color,
    endColor: Color,
    center: Float,
    sigma: Float,
    alpha: Float
): Modifier {
    val shader = remember { RuntimeShader(GAUSSIAN_COLOR_INTERPOLATION_SHADER) }
    val brush = remember { ShaderBrush(shader) }

    return this.drawBehind {
        shader.setFloatUniform(
            "startColor",
            startColor.red,
            startColor.green,
            startColor.blue,
            startColor.alpha
        )
        shader.setFloatUniform(
            "endColor",
            endColor.red,
            endColor.green,
            endColor.blue,
            endColor.alpha
        )
        shader.setFloatUniform(
            "iResolution",
            this.size.width,
            this.size.height
        )

        shader.setFloatUniform("center", center)
        shader.setFloatUniform("sigma", sigma)

        drawRect(brush = brush, alpha = alpha)
    }
}

private fun Modifier.smoothGradientMaskFallback(color: Color, alpha: Float): Modifier {
    return this.background(
        verticalGradient(
            colorStops = arrayOf(
                0.0f to color.copy(alpha = 0.00f),
                0.097f to color.copy(alpha = 0.01f),
                0.2021f to color.copy(alpha = 0.02f),
                0.3034f to color.copy(alpha = 0.03f),
                0.4005f to color.copy(alpha = 0.04f),
                0.4928f to color.copy(alpha = 0.05f),
                0.5799f to color.copy(alpha = 0.07f),
                0.6613f to color.copy(alpha = 0.08f),
                0.7363f to color.copy(alpha = 0.10f),
                0.8047f to color.copy(alpha = 0.12f),
                0.8657f to color.copy(alpha = 0.14f),
                0.9189f to color.copy(alpha = 0.16f),
                0.9639f to color.copy(alpha = 0.18f),
                1.0f to color.copy(alpha = 0.20f)
            )
        ), alpha = alpha
    )
}
