package com.nevoit.cresto.ui.theme.glasense

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val easingBlackGradientMask = Brush.verticalGradient(
    colorStops = arrayOf(
        // CSS: rgba(0, 0, 0, 0.00) at -23.53%, -12.22%, -1.12%
        // 我们将它定义为渐变的起始颜色
        0.0f to Color.Black.copy(alpha = 0.00f),

        // 后续的每一个点都精确对应
        0.097f to Color.Black.copy(alpha = 0.01f),    // 9.7%
        0.2021f to Color.Black.copy(alpha = 0.02f),   // 20.21%
        0.3034f to Color.Black.copy(alpha = 0.03f),   // 30.34%
        0.4005f to Color.Black.copy(alpha = 0.04f),   // 40.05%
        0.4928f to Color.Black.copy(alpha = 0.05f),   // 49.28%
        0.5799f to Color.Black.copy(alpha = 0.07f),   // 57.99%
        0.6613f to Color.Black.copy(alpha = 0.08f),   // 66.13%
        0.7363f to Color.Black.copy(alpha = 0.10f),   // 73.63%
        0.8047f to Color.Black.copy(alpha = 0.12f),   // 80.47%
        0.8657f to Color.Black.copy(alpha = 0.14f),   // 86.57%
        0.9189f to Color.Black.copy(alpha = 0.16f),   // 91.89%
        0.9639f to Color.Black.copy(alpha = 0.18f),   // 96.39%
        1.0f to Color.Black.copy(alpha = 0.20f)       // 100%
    )
)