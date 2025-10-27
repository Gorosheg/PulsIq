package gorosheg.pulsiq.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import gorosheg.pulsiq.settings.R
import gorosheg.pulsiq.settings.ui.model.SettingsUiState
import gorosheg.pulsiq.ui.BlueGray
import gorosheg.pulsiq.ui.White

@Composable
internal fun SettingsScreenContent(
    state: SettingsUiState,
    onSettingClick: (setting: SettingsUiState.SettingItem) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(state.settingItems) { setting ->
            SettingCard(setting = setting, onClick = { onSettingClick(setting) })
        }
    }
}

@Composable
private fun SettingCard(
    setting: SettingsUiState.SettingItem,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
            ) { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BlueGray),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(setting.title),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = White
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, name = "SettingsScreenContent Preview")
@Composable
private fun SettingsScreenContentPreview() {
    gorosheg.pulsiq.ui.MyAppTheme {
        SettingsScreenContent(
            state = SettingsUiState(
                settingItems = listOf(
                    SettingsUiState.SettingItem.ThresholdSettings(
                        lowerThreshold = 140,
                        upperThreshold = 180,
                        minimumThresholdText = R.string.min_threshold_title,
                        maximumThresholdText = R.string.max_threshold_title,
                    ),
                    SettingsUiState.SettingItem.SoundVibration(
                        soundEnabled = true,
                        vibrationEnabled = false,
                        soundSwitchText = R.string.sound,
                        vibrationSwitchText = R.string.vibration,
                    ),
                    SettingsUiState.SettingItem.DeviceConnection
                )
            ),
            onSettingClick = {}
        )
    }
}
