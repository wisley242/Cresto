package com.nevoit.cresto.ui.menu

import androidx.compose.ui.graphics.painter.Painter

data class MenuItemData(
    val text: String,
    val icon: Painter,
    val isDestructive: Boolean = false,
    val onClick: () -> Unit
)

data class MenuState(
    val isVisible: Boolean = false,
    val anchorPosition: androidx.compose.ui.geometry.Offset = androidx.compose.ui.geometry.Offset.Zero,
    val items: List<MenuItemData> = emptyList()
)

