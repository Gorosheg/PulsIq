package gorosheg.pulsiq.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EditNameDialog(
    name: String,
    onCloseEditDialogClick: () -> Unit,
    onNameChanged: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    AlertDialog(
        onDismissRequest = {
            keyboardController?.hide()
            focusManager.clearFocus(force = true)
            onCloseEditDialogClick.invoke()
        },
        confirmButton = {},
        text = {
            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
                keyboardController?.show()
            }
            Column {
                val initial = remember(name) {
                    TextFieldValue(
                        text = name,
                        selection = TextRange(name.length)
                    )
                }
                val textFieldValueState = remember { mutableStateOf(initial) }

                TextField(
                    value = textFieldValueState.value,
                    onValueChange = {
                        textFieldValueState.value = it
                        onNameChanged.invoke(it.text)
                    },
                    modifier = Modifier.focusRequester(focusRequester),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus(force = true)
                            onCloseEditDialogClick.invoke()
                        }
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                BlueButton(
                    text = R.string.save,
                    onClick = {
                        onNameChanged.invoke(textFieldValueState.value.text)
                        keyboardController?.hide()
                        focusManager.clearFocus(force = true)
                        onCloseEditDialogClick.invoke()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}

@Preview
@Composable
private fun EditNameDialogPreview() {
    MyAppTheme {
        EditNameDialog(
            name = "Тренировка",
            onCloseEditDialogClick = {},
            onNameChanged = {},
        )
    }
}