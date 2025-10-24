package com.nevoit.cresto.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

@Composable
fun BottomSheet(
    visible: Boolean,
    containerColor: Color,
    onDismiss: () -> Unit,
    onClose: () -> Unit,
    shape: Shape,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    if (visible) {
        Box(modifier = Modifier.fillMaxSize()) {
            /*val scrimAlpha = if (sheetHeight > 0) {
                (1f - (offsetY.value / sheetHeight)).coerceIn(0f, 0.5f)
            } else {
                0f
            }*/
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        enabled = visible,
                        onClick = onClose
                    )
            )

            Box(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.systemBars)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        shape = shape,
                        color = containerColor
                    )
            ) {

                Column(
                    modifier = Modifier.imePadding()
                ) {
                    content()
                }
            }
        }
    }
}