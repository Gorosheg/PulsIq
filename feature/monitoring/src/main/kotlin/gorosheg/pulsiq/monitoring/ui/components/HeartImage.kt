package gorosheg.pulsiq.monitoring.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gorosheg.pulsiq.ui.DarkGray

@Composable
internal fun Heart(
    pulse: Int,
    heartColor: Color,
    scaleAnimation: Float,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = null,
            modifier = Modifier
                .size(280.dp)
                .scale(scaleAnimation),
            tint = heartColor
        )
        Text(
            text = pulse.toString(),
            fontSize = 72.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGray
        )
    }
}