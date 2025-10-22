package cl.travy.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import cl.travy.app.ui.theme.DarkBlue
import cl.travy.app.ui.theme.DarkGrey
import cl.travy.app.ui.theme.LightBlue
import cl.travy.app.ui.theme.LightGrey
import cl.travy.app.ui.theme.Typography

private val DarkColorScheme = darkColorScheme(
    primary = DarkBlue,
    secondary = DarkGrey,

    )

private val LightColorScheme = lightColorScheme(
    primary = LightBlue,
    secondary = LightGrey,

    )

@Composable
fun TravyAppTheme(

    content: @Composable () -> Unit
) {

    val colorScheme = LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}