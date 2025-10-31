package com.nevoit.cresto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.nevoit.cresto.ui.TodoScreen
import com.nevoit.cresto.ui.overscroll.OffsetOverscrollFactory
import com.nevoit.cresto.ui.theme.glasense.GlasenseTheme

/**
 * The main activity of the application.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This makes the app display behind the system bars.
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            GlasenseTheme {
                val animationScope = rememberCoroutineScope()
                // Create a custom overscroll factory.
                val overscrollFactory = remember {
                    OffsetOverscrollFactory(
                        orientation = Orientation.Vertical,
                        animationScope = animationScope,
                    )
                }

                // Provide the custom overscroll factory to the composable tree.
                CompositionLocalProvider(
                    LocalOverscrollFactory provides overscrollFactory
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        // Display the main screen of the application.
                        TodoScreen()
                    }
                }
            }
        }
    }
}