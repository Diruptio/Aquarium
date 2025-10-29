package diruptio.aquarium.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import diruptio.aquarium.core.Fish
import diruptio.aquarium.ui.component.Button
import diruptio.aquarium.ui.component.Dialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun FishAddDialog(coroutineScope: CoroutineScope,
                  fish: Fish? = null,
                  open: Boolean = true,
                  close: (() -> Unit)? = null,
                  submit: (suspend (fish: Fish) -> Unit)? = null) {
    Dialog(open, close) {
        var loading by remember { mutableStateOf(false) }
        var name by remember { mutableStateOf(fish?.name?: "") }
        var url by remember { mutableStateOf(fish?.url?: "") }
        var secret by remember { mutableStateOf(fish?.secret?: "") }

        Text(
            text = if (fish == null) "Add a new Fish" else "Edit Fish ${fish.name}",
            style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            enabled = !loading,
            label = { Text("Name") },
            maxLines = 1)

        OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            enabled = !loading,
            label = { Text("URL") },
            maxLines = 1)

        OutlinedTextField(
            value = secret,
            onValueChange = { secret = it },
            enabled = !loading,
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Secret") },
            maxLines = 1)

        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Button(
                onClick = close ?: {},
                enabled = !loading,
                modifier = Modifier.padding()) {
                Text("Cancel")
            }
            Button(
                onClick = {
                    coroutineScope.launch {
                        submit?.invoke(Fish(name, url, secret))
                        close?.invoke()
                    }
                },
                enabled = !loading,
                modifier = Modifier.padding()) {
                Text(if (fish == null) "Add Fish" else "Save Changes")
            }
        }
    }
}
