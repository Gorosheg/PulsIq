package gorosheg.pulsiq.settings.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gorosheg.pulsiq.settings.R
import gorosheg.pulsiq.ui.MyAppTheme
import gorosheg.pulsiq.ui.White

@Composable
internal fun ThresholdSelector(
    title: String,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    val currentValue by rememberUpdatedState(value)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = White
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ThresholdButton(
                icon = Icons.Filled.KeyboardArrowDown,
                contentDescription = stringResource(R.string.decrease_button_description),
                onStep = {
                    if (currentValue > 0) onValueChange(currentValue - 1)
                }
            )

            Text(
                text = currentValue.toString(),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            ThresholdButton(
                icon = Icons.Filled.KeyboardArrowUp,
                contentDescription = stringResource(R.string.increase_button_description),
                onStep = {
                    onValueChange((currentValue + 1).coerceAtMost(250))
                }
            )
        }
    }
}


@Preview
@Composable
private fun ThresholdSelectorPreview() {
    MyAppTheme {
        ThresholdSelector(
            title = "Threshold",
            value = 75,
            onValueChange = {}
        )
    }
}
