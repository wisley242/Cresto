package com.nevoit.cresto.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.capsule.ContinuousRoundedRectangle
import com.nevoit.cresto.util.g2

@Composable
fun ConfigInfoHeader(
    brush: Brush? = null,
    color: Color? = null,
    backgroundColor: Color,
    icon: Painter,
    title: String,
    info: String,
    enableGlow: Boolean? = false
) {
    Column(
        modifier = Modifier
            .background(color = backgroundColor, shape = ContinuousRoundedRectangle(12.dp, g2))
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(top = 24.dp)
                .size(48.dp)
                .then(
                    if (brush != null) Modifier.background(
                        brush = brush,
                        shape = CircleShape
                    ) else if (color != null) Modifier.background(
                        color = color,
                        shape = CircleShape
                    ) else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            if (enableGlow == true) {
                Icon(
                    painter = icon,
                    tint = Color.White.copy(.5f),
                    contentDescription = "$title config entry",
                    modifier = Modifier
                        .graphicsLayer { blendMode = BlendMode.Plus }
                        .fillMaxSize()
                        .blur(2.dp)
                )
            }
            Icon(
                painter = icon,
                tint = Color.White,
                contentDescription = "$title config entry",
                modifier = Modifier
                    .graphicsLayer { blendMode = BlendMode.Plus }
                    .fillMaxSize()
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxWidth(),
            maxLines = 1,
            textAlign = TextAlign.Center
        )
        Text(
            text = info,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(start = 24.dp, top = 0.dp, end = 24.dp, bottom = 20.dp)
                .fillMaxWidth()
                .alpha(.5f)
        )
    }
}