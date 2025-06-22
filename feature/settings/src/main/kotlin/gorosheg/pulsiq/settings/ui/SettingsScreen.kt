package gorosheg.pulsiq.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import gorosheg.pulsiq.settings.presentation.SettingsViewModel
import gorosheg.pulsiq.settings.ui.model.SettingsUiState
import gorosheg.pulsiq.ui.Black
import org.koin.androidx.compose.getViewModel

internal class SettingsScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = getViewModel<SettingsViewModel>()
        val state by viewModel.uiState.collectAsState()

        val selectedSetting = remember { mutableStateOf<SettingsUiState.SettingItem?>(null) }
        val lowerThreshold = remember { mutableIntStateOf(100) }
        val upperThreshold = remember { mutableIntStateOf(180) }

        SettingsScreenContent(
            state = state,
            onSettingClick = { id ->
                val setting = state.settingItems.firstOrNull { it.id == id }
                selectedSetting.value = setting
            }
        )

        selectedSetting.value?.let {
            AlertDialog(
                onDismissRequest = { selectedSetting.value = null },
                confirmButton = {},
                dismissButton = {},
                title = {},
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Минимальный порог",
                            style = MaterialTheme.typography.titleMedium,
                            color = Black
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            IconButton(
                                onClick = { if (lowerThreshold.intValue > 0) lowerThreshold.intValue -= 1 },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Decrease",
                                    tint = Black
                                )
                            }
                            Text(
                                text = "${lowerThreshold.intValue}",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                            IconButton(
                                onClick = { lowerThreshold.intValue += 1 },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Increase",
                                    tint = Black
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Максимальный порог",
                            style = MaterialTheme.typography.titleMedium,
                            color = Black
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            IconButton(
                                onClick = { if (upperThreshold.intValue > 0) upperThreshold.intValue -= 1 },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Decrease",
                                    tint = Black
                                )
                            }
                            Text(
                                text = "${upperThreshold.intValue}",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                            IconButton(
                                onClick = { upperThreshold.intValue += 1 },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Increase",
                                    tint = Black
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = {
                                // Действие по применению — скажешь позже
                                selectedSetting.value = null
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Применить")
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
            )
        }
    }
}