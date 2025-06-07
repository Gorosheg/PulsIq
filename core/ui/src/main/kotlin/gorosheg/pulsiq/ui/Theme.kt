package gorosheg.pulsiq.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = Blue,
    onPrimary = Color.White,
    background = DarkGray,
    onBackground = Color.White
)

@Composable
fun MyAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColors,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}
