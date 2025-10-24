package com.nevoit.cresto.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner

data class WindowManagerProperties(
    val gravity: Int = Gravity.CENTER,
    val flags: Int = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
    val width: Int = WindowManager.LayoutParams.MATCH_PARENT,
    val height: Int = WindowManager.LayoutParams.MATCH_PARENT
)

@Composable
fun WindowManagerComposable(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    properties: WindowManagerProperties = WindowManagerProperties(),
    content: @Composable () -> Unit
) {
    if (!visible) return

    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val shapes = MaterialTheme.shapes

    val view = LocalView.current
    val context = LocalContext.current
    val windowManager =
        remember { context.getSystemService(Context.WINDOW_SERVICE) as WindowManager }

    val interceptingView = remember(context) {
        BackPressInterceptingView(context).apply {
            composeView.setContent(
                {
                    MaterialTheme(
                        colorScheme = colorScheme,
                        typography = typography,
                        shapes = shapes
                    ) {
                        content()
                    }
                })

        }
    }

    LaunchedEffect(onDismissRequest) {
        interceptingView.onDismissRequest = onDismissRequest
    }

    DisposableEffect(Unit) {
        view.findViewTreeLifecycleOwner()?.let { interceptingView.setViewTreeLifecycleOwner(it) }
        view.findViewTreeViewModelStoreOwner()
            ?.let { interceptingView.setViewTreeViewModelStoreOwner(it) }
        view.findViewTreeSavedStateRegistryOwner()
            ?.let { interceptingView.setViewTreeSavedStateRegistryOwner(it) }

        val params = WindowManager.LayoutParams(
            properties.width,
            properties.height,
            WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
            properties.flags,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = properties.gravity
            token = view.applicationWindowToken
        }

        windowManager.addView(interceptingView, params)

        onDispose {
            windowManager.removeView(interceptingView)
        }
    }
}

@SuppressLint("ViewConstructor")
private class BackPressInterceptingView(context: Context) : FrameLayout(context) {
    val composeView = ComposeView(context)
    var onDismissRequest: (() -> Unit)? = null

    init {
        addView(composeView)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
            onDismissRequest?.invoke()
            return true
        }
        return super.dispatchKeyEvent(event)
    }
}