package com.nevoit.cresto.ui.theme.glasense

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object AppButtonColors {
    @Composable
    fun primary() = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1F),
        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3F)
    )

    @Composable
    fun secondary() = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05F),
        contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5F),
        // ... 其他颜色配置
    )
}

object NavigationButtonActiveColors {
    @Composable
    fun primary() = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    )
}

object NavigationButtonNormalColors {
    @Composable
    fun primary() = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.background.copy(0f),
        contentColor = MaterialTheme.colorScheme.onSurface.copy(0.8f)
    )
}