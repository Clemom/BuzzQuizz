package clem.project.buzz.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val LightColors = lightColorScheme(
    primary      = BWBlack,
    onPrimary    = BWWhite,
    background   = BWWhite,
    surface      = BWWhite,
    onSurface    = BWBlack,
    error        = BWBlack // reuse black for emphasis
)

private val DarkColors = darkColorScheme(
    primary      = BWWhite,
    onPrimary    = BWBlack,
    background   = BWBlack,
    surface      = BWBlack,
    onSurface    = BWWhite,
    error        = BWWhite
)

private val BWShapes = Shapes(
    small  = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large  = RoundedCornerShape(16.dp)
)

@Composable
fun BuzzQuizzTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography  = Typography,  // can keep default typography
        shapes      = BWShapes,
        content     = content
    )
}