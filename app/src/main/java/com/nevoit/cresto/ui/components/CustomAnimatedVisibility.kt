package com.nevoit.cresto.ui.components

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Fade(val animationSpec: FiniteAnimationSpec<Float>)

data class Scale(
    val animationSpec: FiniteAnimationSpec<Float>,
    val scale: Float
)

data class Blur(
    val animationSpec: FiniteAnimationSpec<Dp>,
    val radius: Dp
)

data class MyEnterTransition(
    val fade: Fade? = null,
    val scale: Scale? = null,
    val blur: Blur? = null
) {
    operator fun plus(other: MyEnterTransition): MyEnterTransition {
        return MyEnterTransition(
            fade = other.fade ?: this.fade,
            scale = other.scale ?: this.scale,
            blur = other.blur ?: this.blur
        )
    }
}

data class MyExitTransition(
    val fade: Fade? = null,
    val scale: Scale? = null,
    val blur: Blur? = null
) {
    operator fun plus(other: MyExitTransition): MyExitTransition {
        return MyExitTransition(
            fade = other.fade ?: this.fade,
            scale = other.scale ?: this.scale,
            blur = other.blur ?: this.blur
        )
    }
}


fun myFadeIn(animationSpec: FiniteAnimationSpec<Float> = tween(300)): MyEnterTransition =
    MyEnterTransition(fade = Fade(animationSpec = animationSpec))

fun myScaleIn(
    animationSpec: FiniteAnimationSpec<Float> = spring(stiffness = Spring.StiffnessMedium),
    initialScale: Float = 0.9f
): MyEnterTransition =
    MyEnterTransition(scale = Scale(animationSpec = animationSpec, scale = initialScale))

fun myBlurIn(
    animationSpec: FiniteAnimationSpec<Dp> = tween(300),
    initialRadius: Dp = 8.dp
): MyEnterTransition =
    MyEnterTransition(blur = Blur(animationSpec = animationSpec, radius = initialRadius))

fun myFadeOut(animationSpec: FiniteAnimationSpec<Float> = tween(300)): MyExitTransition =
    MyExitTransition(fade = Fade(animationSpec = animationSpec))

fun myScaleOut(
    animationSpec: FiniteAnimationSpec<Float> = spring(stiffness = Spring.StiffnessMedium),
    targetScale: Float = 0.9f
): MyExitTransition =
    MyExitTransition(scale = Scale(animationSpec = animationSpec, scale = targetScale))

fun myBlurOut(
    animationSpec: FiniteAnimationSpec<Dp> = tween(300),
    targetRadius: Dp = 8.dp
): MyExitTransition =
    MyExitTransition(blur = Blur(animationSpec = animationSpec, radius = targetRadius))

@Composable
fun CustomAnimatedVisibility(
    visibleState: MutableTransitionState<Boolean>,
    modifier: Modifier = Modifier,
    enter: MyEnterTransition,
    exit: MyExitTransition,
    content: @Composable () -> Unit
) {
    val transition = rememberTransition(visibleState, label = "MyAnimatedVisibility")

    if (visibleState.currentState || visibleState.targetState) {

        val hiddenStateScale = when {
            visibleState.targetState -> enter.scale?.scale ?: 1f
            else -> exit.scale?.scale ?: 1f
        }

        val hiddenStateBlur = when {
            visibleState.targetState -> enter.blur?.radius ?: 0.dp
            else -> exit.blur?.radius ?: 0.dp
        }

        val alpha by transition.animateFloat(
            label = "alpha",
            transitionSpec = {
                if (targetState) enter.fade?.animationSpec ?: tween()
                else exit.fade?.animationSpec ?: tween()
            }
        ) { isVisible -> if (isVisible) 1f else 0f }

        val scale by transition.animateFloat(
            label = "scale",
            transitionSpec = {
                if (targetState) enter.scale?.animationSpec ?: spring()
                else exit.scale?.animationSpec ?: spring()
            }
        ) { isVisible ->
            if (isVisible) 1f else hiddenStateScale
        }

        val blurRadius by transition.animateDp(
            label = "blur",
            transitionSpec = {
                if (targetState) enter.blur?.animationSpec ?: tween()
                else exit.blur?.animationSpec ?: tween()
            }
        ) { isVisible ->
            if (isVisible) 0.dp else hiddenStateBlur
        }

        Box(
            modifier = modifier
                .then(if (blurRadius > 0.dp) Modifier.blur(radius = blurRadius) else Modifier)
                .graphicsLayer {
                    this.alpha = alpha
                    this.scaleX = scale
                    this.scaleY = scale
                }
        ) {
            content()
        }
    }
}

@Composable
fun CustomAnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    enter: MyEnterTransition,
    exit: MyExitTransition,
    content: @Composable () -> Unit
) {
    val visibleState = remember { MutableTransitionState(initialState = false) }

    LaunchedEffect(visible) {
        visibleState.targetState = visible
    }

    CustomAnimatedVisibility(visibleState, modifier, enter, exit, content)
}