package gorosheg.pulsiq.settings.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun SettingsScreenContent(
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        text = "Настройки",
        color = Color.White,
        fontSize = 20.sp,
    )
}