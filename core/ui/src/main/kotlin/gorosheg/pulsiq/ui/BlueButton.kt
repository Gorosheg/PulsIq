package gorosheg.pulsiq.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BlueButton(
    modifier: Modifier = Modifier,
    text: Int,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Blue,
            contentColor = White
        ),
        shape = RoundedCornerShape(8.dp),
        enabled = enabled,
        modifier = modifier
    ) {
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview
@Composable
private fun BlueButtonPreview() {
    MyAppTheme {
        BlueButton(
            text = R.string.save,
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

