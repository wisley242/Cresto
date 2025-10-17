package com.nevoit.cresto.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.nevoit.cresto.ui.theme.glasense.Blue500
import com.nevoit.cresto.ui.theme.glasense.Gray500
import com.nevoit.cresto.ui.theme.glasense.Green500
import com.nevoit.cresto.ui.theme.glasense.Orange500
import com.nevoit.cresto.ui.theme.glasense.Purple500
import com.nevoit.cresto.ui.theme.glasense.Red500
import com.nevoit.cresto.ui.theme.glasense.Yellow500
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun HorizontalFlagPicker(
    selectedIndex: Int,
    onIndexSelected: (Int) -> Unit,
) {
    val colors = listOf(
        Color.Transparent,
        Red500,
        Orange500,
        Yellow500,
        Green500,
        Blue500,
        Purple500,
        Gray500
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(14.dp),
    ) {
        itemsIndexed(colors) { index, color ->

            ColorCircle(
                color = color,
                isSelected = (selectedIndex == index),
                onClick = {
                    onIndexSelected(index)
                }
            )

        }
    }
}

@Composable
fun ColorCircle(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val scale = remember { Animatable(1f) }

    val interactionSource = remember { MutableInteractionSource() }

    var isPressed by remember { mutableStateOf(false) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    isPressed = true
                    scale.animateTo(
                        targetValue = 1.4f,
                        animationSpec = tween(100)
                    )
                }

                is PressInteraction.Release -> {
                    isPressed = true
                    coroutineScope {
                        launch {
                            scale.animateTo(
                                targetValue = 0.8f,
                                animationSpec = tween(300)
                            )
                        }
                        launch {
                            delay(100)
                            onClick()
                        }
                    }
                }

                is PressInteraction.Cancel -> {
                    isPressed = false
                    scale.animateTo(
                        targetValue = 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
            .size(20.dp)
            .background(color, CircleShape)
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {

            }
            .then(
                if (isSelected || isPressed) {
                    Modifier.drawBehind {
                        drawCircle(
                            color = Color.Black.copy(alpha = 0.2f),
                            style = Stroke(width = 4.dp.toPx())
                        )
                    }
                } else {
                    Modifier
                }
            )
    )
}

private fun getPresetTypeForDate(date: LocalDate?): Int {
    if (date == null) {
        return 0
    }
    val today = LocalDate.now()
    return when (date) {
        today -> 1
        today.plusDays(1) -> 2
        today.plusWeeks(1) -> 3
        else -> 4
    }
}


@Composable
fun HorizontalPresetDatePicker(
    initialDate: LocalDate?,
    onDateSelected: (LocalDate?) -> Unit
) {
    var selectedDate by remember { mutableStateOf(initialDate) }
    var selectedPreset by remember { mutableStateOf(getPresetTypeForDate(initialDate)) }
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate?.atStartOfDay(ZoneOffset.UTC)?.toInstant()
            ?.toEpochMilli()
    )

    val presets = listOf(
        "None",
        "Today",
        "Tomorrow",
        "Next Week"
    )
    LaunchedEffect(initialDate) {
        selectedDate = initialDate
        selectedPreset = getPresetTypeForDate(initialDate)
    }

    LazyRow(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                // Prevent from clicking the behind
            },
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
    ) {
        itemsIndexed(presets) { index, text ->
            SelectorBox(
                text = text,
                isSelected = (selectedPreset == index),
                onClick = {
                    val newDate = when (index) {
                        0 -> null
                        1 -> LocalDate.now()
                        2 -> LocalDate.now().plusDays(1)
                        3 -> LocalDate.now().plusWeeks(1)
                        else -> selectedDate // This case won't occur
                    }
                    selectedPreset = index
                    selectedDate = newDate
                    onDateSelected(newDate)
                }
            )
        }
        item {
            Spacer(
                modifier = Modifier
                    .clip(RoundedCornerShape(1.dp))
                    .width(1.5.dp)
                    .height(20.dp)
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f))
            )
        }
        item {
            val isCustomDateSelected = (selectedPreset == 4)
            val buttonText = if (isCustomDateSelected) {
                val date = selectedDate
                date?.format(DateTimeFormatter.ofPattern("MM/dd"))
            } else {
                "Custom"
            }

            DateSelectorBox(
                text = buttonText ?: "Custom",
                isSelected = isCustomDateSelected,
                onClick = { showDatePicker = true }
            )
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                        datePickerState.selectedDateMillis?.let { millis ->
                            val chosenDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneOffset.UTC)
                                .toLocalDate()
                            selectedDate = chosenDate
                            onDateSelected(chosenDate)
                        }
                    },
                    enabled = datePickerState.selectedDateMillis != null
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("取消")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun SelectorBox(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale = remember { Animatable(1f) }

    val interactionSource = remember { MutableInteractionSource() }

    var isPressed by remember { mutableStateOf(false) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    isPressed = true
                    scale.animateTo(
                        targetValue = 1.2f,
                        animationSpec = tween(100)
                    )
                }

                is PressInteraction.Release -> {
                    isPressed = true
                    coroutineScope {
                        launch {
                            scale.animateTo(
                                targetValue = 0.8f,
                                animationSpec = tween(300)
                            )
                        }
                        launch {
                            delay(100)
                            onClick()
                        }
                    }
                }

                is PressInteraction.Cancel -> {
                    isPressed = false
                    scale.animateTo(
                        targetValue = 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
            .clickable(interactionSource = interactionSource, indication = null) {

            }
            .then(
                if (isSelected || isPressed) {
                    Modifier.alpha(1f)
                } else {
                    Modifier.alpha(0.5f)
                }
            )
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun DateSelectorBox(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale = remember { Animatable(1f) }

    val interactionSource = remember { MutableInteractionSource() }

    var isPressed by remember { mutableStateOf(false) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Release -> {
                    isPressed = true
                    onClick()
                }

                is PressInteraction.Cancel -> {
                    isPressed = false
                    scale.animateTo(
                        targetValue = 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
            .clickable(interactionSource = interactionSource, indication = null) {

            }
            .then(
                if (isSelected || isPressed) {
                    Modifier.alpha(1f)
                } else {
                    Modifier.alpha(0.5f)
                }
            )
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}