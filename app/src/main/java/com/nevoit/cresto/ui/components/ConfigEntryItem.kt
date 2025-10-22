package com.nevoit.cresto.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nevoit.cresto.R

@Composable
fun ConfigEntryItem(
    brush: Brush? = null,
    color: Color? = null,
    icon: Painter,
    title: String,
    isSwitch: Boolean? = false,
    enableGlow: Boolean? = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
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
                        .graphicsLayer(blendMode = BlendMode.Plus)
                        .fillMaxSize()
                        .blur(2.dp)
                )
            }
            Icon(
                painter = icon,
                tint = Color.White,
                contentDescription = "$title config entry",
                modifier = Modifier
                    .graphicsLayer(blendMode = BlendMode.Plus)
                    .fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(12.dp))
        if (isSwitch == null || !isSwitch) {
            Icon(
                painter = painterResource(R.drawable.ic_forward),
                tint = MaterialTheme.colorScheme.onBackground.copy(.2f),
                contentDescription = "Enter icon",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .height(40.dp)
                    .width(20.dp)
            )
        }
    }
}