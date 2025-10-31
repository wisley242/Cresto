package com.nevoit.cresto.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nevoit.cresto.ui.menu.MenuItemData

@Composable
fun AppNavHost(
    navController: NavHostController,
    showMenu: (anchorPosition: androidx.compose.ui.geometry.Offset, items: List<MenuItemData>) -> Unit,
    viewModel: TodoViewModel
) {
    val fadeDuration = 0
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
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
            HomeScreen(showMenu = showMenu, viewModel = viewModel)
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
            SettingsScreen()
        }
    }
}
