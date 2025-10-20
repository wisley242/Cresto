package com.nevoit.cresto.util

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun getNavigationBarHeight(): Dp {
    val navigationBarInsets = WindowInsets.navigationBars
    with(LocalDensity.current) {
        return navigationBarInsets.getBottom(this).toDp()
    }
}

@Composable
fun getStatusBarHeight(): Dp {
    return WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
}

