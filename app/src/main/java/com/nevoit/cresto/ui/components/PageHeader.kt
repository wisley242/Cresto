package com.nevoit.cresto.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PageHeader(
    title: String,
    statusBarHeight: Dp
) {
    Box(
        modifier = Modifier
            .padding(top = statusBarHeight)
            .height(128.dp + statusBarHeight)
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .padding(start = 12.dp, bottom = 16.dp)
                .align(Alignment.BottomStart)
        )
    }
}