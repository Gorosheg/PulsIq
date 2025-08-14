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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.example.storage.ThresholdsRepository
import gorosheg.pulsiq.settings.R
import gorosheg.pulsiq.settings.presentation.SettingsViewModel
import gorosheg.pulsiq.settings.ui.model.SettingsUiState
import gorosheg.pulsiq.ui.Black
import gorosheg.pulsiq.ui.Blue
import gorosheg.pulsiq.ui.White
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

internal class SettingsScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getViewModel<SettingsViewModel>()
        val state by viewModel.uiState.collectAsState()
        val thresholdsRepository = get<ThresholdsRepository>()

        val lowerThreshold = remember { mutableIntStateOf(thresholdsRepository.getLowerThreshold()) }
        val upperThreshold = remember { mutableIntStateOf(thresholdsRepository.getUpperThreshold()) }
        val selectedSetting = remember { mutableStateOf<SettingsUiState.SettingItem?>(null) }

        SettingsScreenContent(
            state = state,
            onSettingClick = { id ->
                val setting = state.settingItems.firstOrNull { it.id == id }
                selectedSetting.value = setting
            }
        )

        selectedSetting.value?.let { setting ->
            ThresholdSettingsDialog(
                lowerThreshold = lowerThreshold,
                upperThreshold = upperThreshold,
                onDismiss = { selectedSetting.value = null },
                onApply = { lower, upper ->
                    thresholdsRepository.saveThresholds(lower = lower, upper = upper)
                    selectedSetting.value = null
                }
            )
        }
    }
}

@Composable
private fun ThresholdSettingsDialog(
    lowerThreshold: androidx.compose.runtime.MutableState<Int>,
    upperThreshold: androidx.compose.runtime.MutableState<Int>,
    onDismiss: () -> Unit,
    onApply: (Int, Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ThresholdSelector(
                    title = stringResource(R.string.min_threshold_title),
                    value = lowerThreshold.value,
                    onValueChange = { lowerThreshold.value = it }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                ThresholdSelector(
                    title = stringResource(R.string.max_threshold_title),
                    value = upperThreshold.value,
                    onValueChange = { upperThreshold.value = it }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                ApplyButton(
                    onClick = {
                        onApply(lowerThreshold.value, upperThreshold.value)
                    }
                )
            }
        },
        shape = RoundedCornerShape(16.dp),
    )
}

@Composable
private fun ThresholdSelector(
    title: String,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = White
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            ThresholdButton(
                icon = Icons.Default.KeyboardArrowDown,
                contentDescription = stringResource(R.string.decrease_button_description),
                onClick = {
                    if (value > 0) onValueChange(value - 1)
                }
            )
            
            Text(
                text = "$value",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            
            ThresholdButton(
                icon = Icons.Default.KeyboardArrowUp,
                contentDescription = stringResource(R.string.increase_button_description),
                onClick = {
                    onValueChange(value + 1)
                }
            )
        }
    }
}

@Composable
private fun ThresholdButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(48.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = White
        )
    }
}

@Composable
private fun ApplyButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Blue),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = stringResource(R.string.apply_button_text),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}