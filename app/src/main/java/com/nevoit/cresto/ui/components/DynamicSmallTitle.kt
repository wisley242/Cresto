package com.nevoit.cresto.ui.components

import android.os.Build
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nevoit.cresto.ui.gaussiangradient.smoothGradientMask
import com.nevoit.cresto.ui.gaussiangradient.smoothGradientMaskFallbackInvert
import com.nevoit.cresto.ui.theme.glasense.linearGradientMaskT2B70
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect

@OptIn(ExperimentalHazeApi::class)
@Composable
fun DynamicSmallTitle(
    modifier: Modifier,
    title: String,
    statusBarHeight: Dp,
    isVisible: Boolean,
    hazeState: HazeState,
    surfaceColor: Color,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .height(48.dp + statusBarHeight + 48.dp)
            .fillMaxWidth()
    ) {
        CustomAnimatedVisibility(
            visible = isVisible,
            enter = myFadeIn(),
            exit = myFadeOut()
        ) {
            Box(
                modifier = Modifier
                    .height(48.dp + statusBarHeight + 48.dp)
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .hazeEffect(hazeState) {
                        blurRadius = 2.dp
                        noiseFactor = 0f
                        inputScale = HazeInputScale.Fixed(0.5f)
                        mask = linearGradientMaskT2B70
                    }
                    .then(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Modifier.smoothGradientMask(
                            surfaceColor.copy(alpha = 1f),
                            surfaceColor.copy(alpha = 0f),
                            0.5f,
                            0.5f,
                            0.7f
                        ) else Modifier.smoothGradientMaskFallbackInvert(surfaceColor, 0.7f)
                    )
            ) {}
        }
        content()
        CustomAnimatedVisibility(
            visible = isVisible,
            enter = myScaleIn(
                tween(200, 0, CubicBezierEasing(0.2f, 0.2f, 0f, 1f)),
                0.9f
            ) + myFadeIn(tween(100)),
            exit = myScaleOut(
                tween(200, 0, CubicBezierEasing(0.2f, 0.2f, 0f, 1f)),
                0.9f
            ) + myFadeOut(tween(200)),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = statusBarHeight, bottom = 48.dp)
        ) {
            Text(title, style = MaterialTheme.typography.headlineSmall)
        }
    }
}