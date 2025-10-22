package com.nevoit.cresto.ui.gaussiangradient

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush

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