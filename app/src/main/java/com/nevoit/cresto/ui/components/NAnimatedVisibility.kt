package com.example.yourapp // 请替换成你自己的包名

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// =================================================================================
// 1. 动画数据结构 (字段名已优化)
// =================================================================================

data class Fade(val animationSpec: FiniteAnimationSpec<Float>)

data class Scale(
    val animationSpec: FiniteAnimationSpec<Float>,
    val scale: Float // 中性名称：代表不可见时的缩放值
)

data class Blur(
    val animationSpec: FiniteAnimationSpec<Dp>,
    val radius: Dp // 中性名称：代表不可见时的模糊半径
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

// =================================================================================
// 2. 动画工厂函数 (已更新以匹配新的字段名)
// =================================================================================

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

// =================================================================================
// 3. 核心组件 (已修正动画值的逻辑)
// =================================================================================

private enum class VisibilityState { Visible, Hidden }

@Composable
fun NAnimatedVisibility(
    visibleState: MutableTransitionState<Boolean>,
    modifier: Modifier = Modifier,
    enter: MyEnterTransition = myFadeIn() + myScaleIn(),
    exit: MyExitTransition = myFadeOut() + myScaleOut(),
    content: @Composable () -> Unit
) {
    val transition = rememberTransition(visibleState, label = "MyAnimatedVisibility")

    if (visibleState.currentState || visibleState.targetState) {

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
            if (isVisible) {
                1f
            } else {
                enter.scale?.scale ?: exit.scale?.scale ?: 1f
            }
        }

        val blurRadius by transition.animateDp(
            label = "blur",
            transitionSpec = {
                if (targetState) enter.blur?.animationSpec ?: tween()
                else exit.blur?.animationSpec ?: tween()
            }
        ) { isVisible ->
            if (isVisible) {
                0.dp
            } else {
                enter.blur?.radius ?: exit.blur?.radius ?: 0.dp
            }
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

// --- 便捷的 Boolean 重载版本 (无需改动) ---
@Composable
fun NAnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    enter: MyEnterTransition = myFadeIn() + myScaleIn(),
    exit: MyExitTransition = myFadeOut() + myScaleOut(),
    content: @Composable () -> Unit
) {
    val visibleState = remember { MutableTransitionState(initialState = visible) }
    visibleState.targetState = visible
    NAnimatedVisibility(visibleState, modifier, enter, exit, content)
}
