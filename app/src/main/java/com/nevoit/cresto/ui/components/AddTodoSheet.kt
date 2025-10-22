package com.nevoit.cresto.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousCapsule
import com.kyant.capsule.ContinuousRoundedRectangle
import com.nevoit.cresto.R
import com.nevoit.cresto.ui.theme.glasense.AppButtonColors
import com.nevoit.cresto.ui.theme.glasense.getFlagColor
import com.nevoit.cresto.util.g2
import java.time.LocalDate

enum class SelectedButton {
    DUE_DATE, FLAG, HASHTAG, NONE
}

@Composable
fun AddTodoSheet(
    onAddClick: (String, Int, LocalDate?) -> Unit
) {
    var selectedButton by remember { mutableStateOf(SelectedButton.NONE) }
    val state = rememberTextFieldState()
    val focusRequester = remember { FocusRequester() }
    var selectedIndex by remember { mutableIntStateOf(0) }
    var finalDate by remember { mutableStateOf<LocalDate?>(null) }
    val onAdd = {
        val text = state.text as String
        val dueDate = LocalDate.now()
        if (text.isNotBlank()) {
            onAddClick(text, selectedIndex, finalDate)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 0.dp, 12.dp, 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "New Task",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp)
        )


        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .height(48.dp)
                .background(
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05F),
                    ContinuousRoundedRectangle(12.dp)
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier.padding(16.dp, 0.dp)
            ) {
                BasicTextField(
                    state = state,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    onKeyboardAction = { onAdd() },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                )
            }

        }
        Spacer(modifier = Modifier.height(12.dp))
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            val totalWidth = this.maxWidth

            val collapsedSize = 48.dp
            val spacerSize = 12.dp
            val expandedWidth = totalWidth - (collapsedSize * 1) - 48.dp - (spacerSize * 2)
            val defaultWidth = (totalWidth - 48.dp - (spacerSize * 2)) / 2

            val dueDateWidth by animateDpAsState(
                targetValue = when (selectedButton) {
                    SelectedButton.DUE_DATE -> expandedWidth
                    SelectedButton.NONE -> defaultWidth
                    else -> collapsedSize
                },
                animationSpec = spring(
                    dampingRatio = 0.7f,
                    stiffness = 300f
                )
            )
            val flagWidth by animateDpAsState(
                targetValue = when (selectedButton) {
                    SelectedButton.FLAG -> expandedWidth
                    SelectedButton.NONE -> defaultWidth
                    else -> collapsedSize
                },
                animationSpec = spring(
                    dampingRatio = 0.7f,
                    stiffness = 300f
                )
            )
            val hashtagWidth by animateDpAsState(
                targetValue = when (selectedButton) {
                    SelectedButton.HASHTAG -> expandedWidth
                    SelectedButton.NONE -> defaultWidth
                    else -> collapsedSize
                },
                animationSpec = spring(
                    dampingRatio = 0.7f,
                    stiffness = 300f
                )
            )
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .width(totalWidth - 48.dp - spacerSize)
                        .height(48.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        enabled = true,
                        shape = ContinuousCapsule(g2),
                        onClick = {
                            selectedButton = if (selectedButton == SelectedButton.DUE_DATE) {
                                SelectedButton.NONE
                            } else {
                                SelectedButton.DUE_DATE
                            }
                        },
                        modifier = Modifier
                            .height(48.dp)
                            .width(dueDateWidth),
                        colors = AppButtonColors.secondary(),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            with(this@Button) {
                                AnimatedVisibility(
                                    visible = selectedButton != SelectedButton.DUE_DATE,
                                    enter = fadeIn(animationSpec = tween(delayMillis = 100)) + scaleIn(
                                        animationSpec = tween(delayMillis = 100),
                                        initialScale = 0.9f
                                    ),
                                    exit = fadeOut(animationSpec = tween(durationMillis = 100)) + scaleOut(
                                        animationSpec = tween(delayMillis = 100),
                                        targetScale = 0.9f
                                    )
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_calendar),
                                        contentDescription = "Due Date",
                                        modifier = Modifier.width(28.dp)
                                    )
                                }
                                AnimatedVisibility(
                                    visible = selectedButton == SelectedButton.DUE_DATE,
                                    enter = fadeIn(animationSpec = tween(delayMillis = 100)) + scaleIn(
                                        animationSpec = tween(delayMillis = 100),
                                        initialScale = 0.9f
                                    ),
                                    exit = fadeOut(animationSpec = tween(durationMillis = 100)) + scaleOut(
                                        animationSpec = tween(delayMillis = 100),
                                        targetScale = 0.9f
                                    )
                                ) {
                                    HorizontalPresetDatePicker(
                                        initialDate = finalDate,
                                        onDateSelected = {
                                            finalDate = it
                                            selectedButton = SelectedButton.NONE
                                        }
                                    )
                                }
                            }
                        }

                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        enabled = true,
                        shape = ContinuousCapsule(g2),
                        onClick = {
                            selectedButton = if (selectedButton == SelectedButton.FLAG) {
                                SelectedButton.NONE
                            } else {
                                SelectedButton.FLAG
                            }
                        },
                        modifier = Modifier
                            .height(48.dp)
                            .width(flagWidth),
                        colors = AppButtonColors.secondary(),
                        contentPadding = PaddingValues(0.dp),
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            with(this@Button) {
                                AnimatedVisibility(
                                    visible = selectedButton != SelectedButton.FLAG,
                                    enter = fadeIn(animationSpec = tween(delayMillis = 100)) + scaleIn(
                                        animationSpec = tween(delayMillis = 100),
                                        initialScale = 0.9f
                                    ),
                                    exit = fadeOut(animationSpec = tween(durationMillis = 100)) + scaleOut(
                                        animationSpec = tween(delayMillis = 100),
                                        targetScale = 0.9f
                                    )
                                ) {
                                    val displayColor = getFlagColor(selectedIndex)
                                    Icon(
                                        painter = if (displayColor == Color.Transparent) {
                                            painterResource(id = R.drawable.ic_flag)
                                        } else {
                                            painterResource(id = R.drawable.ic_flag_fill)
                                        },
                                        contentDescription = "Flag",
                                        modifier = Modifier.width(28.dp),
                                        tint = if (displayColor == Color.Transparent) {
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5F)
                                        } else {
                                            displayColor
                                        }
                                    )
                                }
                                AnimatedVisibility(
                                    visible = selectedButton == SelectedButton.FLAG,
                                    enter = fadeIn(animationSpec = tween(delayMillis = 100)) + scaleIn(
                                        animationSpec = tween(delayMillis = 100),
                                        initialScale = 0.9f
                                    ),
                                    exit = fadeOut(animationSpec = tween(durationMillis = 100)) + scaleOut(
                                        animationSpec = tween(delayMillis = 100),
                                        targetScale = 0.9f
                                    )
                                ) {

                                    HorizontalFlagPicker(
                                        selectedIndex = selectedIndex,
                                        onIndexSelected = { newIndex ->
                                            selectedIndex = newIndex
                                            selectedButton = SelectedButton.NONE
                                        }
                                    )

                                }
                            }
                        }

                    }
                    /*pacer(modifier = Modifier.width(12.dp))
                    Button(
                        enabled = true,
                        shape = ContinuousCapsule(g2),
                        onClick = {
                            selectedButton = if (selectedButton == SelectedButton.HASHTAG) {
                                SelectedButton.NONE
                            } else {
                                SelectedButton.HASHTAG
                            }
                        },
                        modifier = Modifier
                            .height(48.dp)
                            .width(hashtagWidth),
                        colors = AppButtonColors.secondary(),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_hashtag),
                            contentDescription = "Tag",
                            modifier = Modifier.width(28.dp)
                        )
                    }*/

                }
                Button(
                    enabled = true,
                    shape = CircleShape,
                    onClick = onAdd,
                    modifier = Modifier
                        .width(48.dp)
                        .height(48.dp),
                    colors = AppButtonColors.primary(),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .drawBehind {
                                val outline = CircleShape.createOutline(
                                    size = this.size,
                                    layoutDirection = this.layoutDirection,
                                    density = this,
                                )
                                val gradientBrush = verticalGradient(
                                    colorStops = arrayOf(
                                        0.0f to Color.White.copy(alpha = 0.2f),
                                        1.0f to Color.White.copy(alpha = 0.02f)
                                    )
                                )
                                drawOutline(
                                    outline = outline,
                                    brush = gradientBrush,
                                    style = Stroke(width = 3.dp.toPx()),
                                    blendMode = BlendMode.Plus
                                )
                            }, contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_checkmark),
                            contentDescription = "Done",
                            modifier = Modifier.width(28.dp)
                        )
                    }

                }
            }

        }

    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}
