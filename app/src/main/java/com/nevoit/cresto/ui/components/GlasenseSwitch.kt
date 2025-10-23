package com.nevoit.cresto.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousCapsule
import com.nevoit.cresto.ui.theme.glasense.Green500
import com.nevoit.cresto.util.g2

@Composable
fun GlasenseSwitch(
    enabled: Boolean? = true, state: Boolean? = false
) {
    val moveDistance = with(LocalDensity.current) { 18.dp.toPx() }
    Box(
        modifier = Modifier
            .width(46.dp)
            .height(28.dp)
            .background(
                color = if (state == true) Green500 else Color(0xFF787880),
                shape = ContinuousCapsule(
                    g2
                )
            )
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer(translationX = moveDistance)
                .padding(start = 3.dp)
                .align(Alignment.CenterStart)
                .dropShadow(
                    shape = CircleShape,
                    shadow = Shadow(
                        radius = 4.dp,
                        color = Color.Black.copy(.16f),
                        offset = DpOffset(x = 0.dp, y = 2.dp)
                    )
                )
                .size(22.dp)
                .background(
                    color = Color.White,
                    shape = CircleShape
                )
        )
    }
}