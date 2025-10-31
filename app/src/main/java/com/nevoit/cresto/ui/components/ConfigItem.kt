package com.nevoit.cresto.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * A composable that represents a generic configuration item in a settings screen.
 * It consists of a title on the left and a content slot on the right, typically for a control like a switch or a button.
 *
 * @param title The main text label for the configuration item.
 * @param color The color of the title text. Defaults to `Color.Unspecified`, which means it will use the color from the current `LocalContentColor`.
 * @param content A composable lambda that defines the content to be placed at the end of the row (e.g., a switch, a button, or another custom composable).
 */
@Composable
fun ConfigItem(
    title: String,
    color: Color = Color.Unspecified,
    content: @Composable () -> Unit
) {
    // A Row layout to arrange the title and content horizontally.
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // The text label for the configuration item.
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            color = color
        )
        Spacer(modifier = Modifier.width(12.dp))
        // The composable content provided to the function, placed at the end of the row.
        content()
    }
}
