package gorosheg.pulsiq.settings.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gorosheg.pulsiq.settings.R
import gorosheg.pulsiq.settings.ui.model.SettingsUiState
import gorosheg.pulsiq.ui.Blue
import gorosheg.pulsiq.ui.MyAppTheme

@Composable
internal fun PulseThresholdDialog(
    setting: SettingsUiState.SettingItem.ThresholdSettings,
    onDismiss: () -> Unit,
    onApply: (Int, Int) -> Unit
) {
    var currentLowerThreshold by remember { mutableIntStateOf(setting.lowerThreshold) }
    var currentUpperThreshold by remember { mutableIntStateOf(setting.upperThreshold) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ThresholdSelector(
                    title = stringResource(setting.minimumThresholdText),
                    value = currentLowerThreshold,
                    onValueChange = { new ->
                        currentLowerThreshold = new.coerceIn(0, minOf(currentUpperThreshold, 250))
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                ThresholdSelector(
                    title = stringResource(setting.maximumThresholdText),
                    value = currentUpperThreshold,
                    onValueChange = { new ->
                        currentUpperThreshold = new.coerceIn(maxOf(currentLowerThreshold, 0), 250)
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                ApplyButton(
                    onClick = {
                        onApply(currentLowerThreshold, currentUpperThreshold)
                    }
                )
            }
        },
        shape = RoundedCornerShape(16.dp),
    )
}

@Composable
private fun ApplyButton(
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick.invoke() },
        colors = ButtonDefaults.buttonColors(containerColor = Blue),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(R.string.apply),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
private fun PulseThresholdDialogPreview() {
    MyAppTheme {
        val mockSetting = SettingsUiState.SettingItem.ThresholdSettings(
            lowerThreshold = 60,
            upperThreshold = 100,
            minimumThresholdText = R.string.min_threshold_title,
            maximumThresholdText = R.string.max_threshold_title
        )

        PulseThresholdDialog(
            setting = mockSetting,
            onDismiss = {},
            onApply = { _, _ -> }
        )
    }
}