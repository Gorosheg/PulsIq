package gorosheg.pulsiq.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import gorosheg.pulsiq.ui.White

@Composable
internal fun SoundVibrationDialog(
    setting: SettingsUiState.SettingItem.SoundVibration,
    onDismiss: () -> Unit,
    onApply: (Boolean, Boolean) -> Unit
) {
    var currentSoundEnabled by remember { mutableStateOf(setting.soundEnabled) }
    var currentVibrationEnabled by remember { mutableStateOf(setting.vibrationEnabled) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SwitchRow(
                    title = stringResource(setting.soundSwitchText),
                    checked = currentSoundEnabled,
                    onCheckedChange = { currentSoundEnabled = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                SwitchRow(
                    title = stringResource(setting.vibrationSwitchText),
                    checked = currentVibrationEnabled,
                    onCheckedChange = { currentVibrationEnabled = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                ApplyButton(
                    onClick = {
                        onApply(currentSoundEnabled, currentVibrationEnabled)
                    }
                )
            }
        },
        shape = RoundedCornerShape(16.dp),
    )
}

@Composable
private fun SwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = White
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Blue,
                checkedTrackColor = Blue.copy(alpha = 0.5f),
                uncheckedThumbColor = White,
                uncheckedTrackColor = White.copy(alpha = 0.3f)
            )
        )
    }
}

@Composable
private fun ApplyButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Blue,
            contentColor = White
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Применить",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SoundVibrationDialogPreview() {
    val mockSetting = SettingsUiState.SettingItem.SoundVibration(
        soundEnabled = true,
        vibrationEnabled = false,
        soundSwitchText = R.string.sound,
        vibrationSwitchText = R.string.vibration
    )

    SoundVibrationDialog(
        setting = mockSetting,
        onDismiss = {},
        onApply = { _, _ -> }
    )
}