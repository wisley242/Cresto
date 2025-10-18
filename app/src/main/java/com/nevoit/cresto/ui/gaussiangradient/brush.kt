package com.nevoit.cresto.ui.gaussiangradient

import android.graphics.RuntimeShader
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.exp
import kotlin.math.pow

enum class GradientOrientation {
    VERTICAL,
    HORIZONTAL
}

fun createGaussianFallbackBrush(
    startColor: Color,
    centerColor: Color,
    endColor: Color,
    centerPosition: Float = 0.5f,
    sigma: Float = 0.15f,
    orientation: GradientOrientation = GradientOrientation.VERTICAL,
    steps: Int = 10
): Brush {
    val clampedCenter = centerPosition.coerceIn(0f, 1f)
    val clampedSigma = sigma.coerceIn(0.01f, 1f)

    val colorStops = Array(steps + 1) { i ->
        val position = i.toFloat() / steps.toFloat()

        val dist = position - clampedCenter
        val exponent = -0.5 * (dist / clampedSigma).toDouble().pow(2.0)
        val gaussianInfluence = exp(exponent).toFloat()

        val edgeColor = if (position <= clampedCenter) startColor else endColor

        val interpolatedColor = lerp(edgeColor, centerColor, gaussianInfluence)

        position to interpolatedColor
    }

    return when (orientation) {
        GradientOrientation.VERTICAL -> Brush.verticalGradient(colorStops = colorStops)
        GradientOrientation.HORIZONTAL -> Brush.horizontalGradient(colorStops = colorStops)
    }
}

fun Modifier.gaussianColorBackground(
    startColor: Color,
    centerColor: Color,
    endColor: Color,
    centerPosition: Float = 0.5f,
    sigma: Float = 0.15f,
    orientation: GradientOrientation = GradientOrientation.VERTICAL
): Modifier = composed {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val shader = remember { RuntimeShader(GAUSSIAN_COLOR_INTERPOLATION_SHADER) }
        val brush = remember { ShaderBrush(shader) }

        this.drawWithCache {
            shader.setFloatUniform(
                "startColor",
                startColor.red,
                startColor.green,
                startColor.blue,
                startColor.alpha
            )
            shader.setFloatUniform(
                "centerColor",
                centerColor.red,
                centerColor.green,
                centerColor.blue,
                centerColor.alpha
            )
            shader.setFloatUniform(
                "endColor",
                endColor.red,
                endColor.green,
                endColor.blue,
                endColor.alpha
            )
            shader.setFloatUniform("mean", centerPosition)
            shader.setFloatUniform("sigma", sigma)
            shader.setFloatUniform(
                "horizontal",
                if (orientation == GradientOrientation.HORIZONTAL) 1f else 0f
            )
            shader.setFloatUniform("resolution", size.width, size.height)

            onDrawBehind {
                drawRect(brush)
            }
        }
    } else {
        val fallbackBrush = createGaussianFallbackBrush(
            startColor = startColor,
            centerColor = centerColor,
            endColor = endColor,
            centerPosition = centerPosition,
            sigma = sigma,
            orientation = orientation
        )
        this.background(fallbackBrush)
    }
}

@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun GaussianShaderBrushPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text("ShaderBrush 高斯颜色渐变", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))

            Text("蓝 -> 品红 -> 黄 (sigma=0.15)")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .gaussianColorBackground(
                        startColor = Color.Cyan,
                        centerColor = Color.Magenta,
                        endColor = Color.Yellow,
                        sigma = 0.2f
                    )
            )

            Spacer(Modifier.height(16.dp))

            Text("水平方向，中心偏移 (sigma=0.2)")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .gaussianColorBackground(
                        startColor = Color(0xFF1E3C72),
                        centerColor = Color.White,
                        endColor = Color(0xFF2A5298),
                        centerPosition = 0.75f,
                        sigma = 0.2f,
                        orientation = GradientOrientation.HORIZONTAL
                    )
            )
        }
    }
}