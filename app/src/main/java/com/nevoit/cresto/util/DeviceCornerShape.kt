package com.nevoit.cresto.util

import android.view.RoundedCorner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.capsule.AbsoluteContinuousRoundedRectangle
import com.kyant.capsule.concentricInset

/**
 * Creates a shape that mirrors the device's physical screen corners.
 *
 * @param padding Optional padding to inset the shape.
 * @param topLeft Whether to round the top-left corner.
 * @param topRight Whether to round the top-right corner.
 * @param bottomRight Whether to round the bottom-right corner.
 * @param bottomLeft Whether to round the bottom-left corner.
 * @return An [AbsoluteContinuousRoundedRectangle] that can be used as a shape in Composables.
 */
@Composable
fun deviceCornerShape(
    padding: Dp = 0.dp,
    topLeft: Boolean = true,
    topRight: Boolean = true,
    bottomRight: Boolean = true,
    bottomLeft: Boolean = true
): AbsoluteContinuousRoundedRectangle {
    val view = LocalView.current

    val density = LocalDensity.current

    val dp = with(density) {
        1.dp.toPx()
    }

    fun getCornerRadius(position: Int, status: Boolean): Float {
        val insets = view.rootWindowInsets
        return if (!status) {
            0f
        } else {
            val radius = insets.getRoundedCorner(position)?.radius?.toFloat()
            // Provide a default radius if the system doesn't report one or it's too small.
            if (radius == null || radius <= 16 * dp) 16 * dp else radius
        }
    }

    // Remember the calculated shape to avoid recalculation on recomposition.
    return remember(view) {
        val topLeftRadius = getCornerRadius(RoundedCorner.POSITION_TOP_LEFT, topLeft)
        val topRightRadius = getCornerRadius(RoundedCorner.POSITION_TOP_RIGHT, topRight)
        val bottomRightRadius = getCornerRadius(RoundedCorner.POSITION_BOTTOM_RIGHT, bottomRight)
        val bottomLeftRadius = getCornerRadius(RoundedCorner.POSITION_BOTTOM_LEFT, bottomLeft)

        // Create the shape with the determined corner radii.
        AbsoluteContinuousRoundedRectangle(
            topLeft = topLeftRadius,
            topRight = topRightRadius,
            bottomRight = bottomRightRadius,
            bottomLeft = bottomLeftRadius
        ).concentricInset(padding) // Apply padding to the shape.
    }
}
