package com.nevoit.cresto.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

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
            SettingsScreen()
        }
    }
}
