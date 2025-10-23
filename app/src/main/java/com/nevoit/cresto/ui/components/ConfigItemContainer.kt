package com.nevoit.cresto.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.capsule.ContinuousRoundedRectangle
import com.nevoit.cresto.util.g2

@Composable
fun ConfigItemContainer(
    title: String? = null,
    backgroundColor: Color,
    content: @Composable () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (title != null) {
            Text(
                text = title,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(.5f),
                modifier = Modifier
                    .padding(
                        start = 12.dp,
                        top = 0.dp,
                        end = 12.dp,
                        bottom = 12.dp
                    )
                    .fillMaxWidth()
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = backgroundColor, shape = ContinuousRoundedRectangle(12.dp, g2))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                content()
            }
        }
    }
}