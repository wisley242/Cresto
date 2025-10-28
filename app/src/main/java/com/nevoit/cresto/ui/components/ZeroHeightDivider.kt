package com.nevoit.cresto.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ZeroHeightDivider(
    color: Color,
    width: Dp,
    modifier: Modifier = Modifier,
    blendMode: BlendMode
) {
    Spacer(
        modifier = Modifier
            .then(modifier)
            .drawBehind {
                drawLine(
                    color = color,
                    start = Offset(x = 0f, y = 0f),
                    end = Offset(this.size.width, y = 0f),
                    strokeWidth = width.toPx(),
                    blendMode = blendMode
                )
            }
            .fillMaxWidth()
            .height(0.dp))
}