package com.nevoit.cresto.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousRoundedRectangle
import com.nevoit.cresto.data.TodoItem
import com.nevoit.cresto.ui.theme.glasense.CalculatedColor
import com.nevoit.cresto.ui.theme.glasense.getFlagColor
import com.nevoit.cresto.util.g2

@Composable
fun TodoItemRow(
    item: TodoItem,
    onCheckedChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .defaultMinSize(minHeight = 72.dp)
            .fillMaxWidth()
            .background(
                color = CalculatedColor.hierarchicalSurfaceColor,
                shape = ContinuousRoundedRectangle(12.dp, g2),
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(12.dp))
        CustomCheckbox(
            checked = item.isCompleted,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = item.title,
            modifier = Modifier.weight(1f),
            textDecoration = if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None
        )
        Text(
            text = if (item.dueDate != null) item.dueDate.toString() else "",
            modifier = Modifier.weight(1f),
        )
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(getFlagColor(item.flag))
                .width(8.dp)
                .height(8.dp)
        )
        Box(
            modifier = Modifier
                .height(32.dp)
                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f))
        ) {
            item.hashtag?.let { Text(text = it, color = MaterialTheme.colorScheme.onBackground) }
        }
        IconButton(onClick = onDeleteClick) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Task"
            )
        }
    }
}
