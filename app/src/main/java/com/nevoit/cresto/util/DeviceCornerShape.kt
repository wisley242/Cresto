package com.nevoit.cresto.util

import android.view.RoundedCorner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.capsule.AbsoluteContinuousRoundedRectangle
import com.kyant.capsule.concentricInset

@Composable
fun deviceCornerShape(
    padding: Dp = 0.dp,
    topLeft: Boolean = true,
    topRight: Boolean = true,
    bottomRight: Boolean = true,
    bottomLeft: Boolean = true
): AbsoluteContinuousRoundedRectangle {
    val view = LocalView.current
    fun getCornerRadius(position: Int, status: Boolean): Float {
        val insets = view.rootWindowInsets
        return if (!status) {
            0f
        } else {
            val radius = insets.getRoundedCorner(position)?.radius?.toFloat()
            if (radius == null || radius <= 64f) 64f else radius
        }
    }
    return remember(view) {
        val insets = view.rootWindowInsets
        val topLeft = getCornerRadius(RoundedCorner.POSITION_TOP_LEFT, topLeft)
        val topRight = getCornerRadius(RoundedCorner.POSITION_TOP_RIGHT, topRight)
        val bottomRight = getCornerRadius(RoundedCorner.POSITION_BOTTOM_RIGHT, bottomRight)
        val bottomLeft = getCornerRadius(RoundedCorner.POSITION_BOTTOM_LEFT, bottomLeft)

        AbsoluteContinuousRoundedRectangle(
            topLeft = topLeft,
            topRight = topRight,
            bottomRight = bottomRight,
            bottomLeft = bottomLeft
        ).concentricInset(padding)
    }
}
