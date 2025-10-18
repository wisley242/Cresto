package com.nevoit.cresto.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nevoit.cresto.R
import com.nevoit.cresto.ui.components.CustomNavigationButton
import com.nevoit.cresto.ui.theme.glasense.easingBlackGradientMask
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
                .hazeEffect(hazeState) {
                    progressive = HazeProgressive.verticalGradient(
                        startIntensity = 0f,
                        endIntensity = 0.3f
                    )
                }
                .background(brush = easingBlackGradientMask, alpha = 0.8f)
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
