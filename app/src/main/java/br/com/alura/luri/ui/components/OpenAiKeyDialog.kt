package br.com.alura.luri.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.alura.luri.ui.theme.LuriTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenAiKeyDialog(
    onSaveKey: (String) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier
            .clip(shape = RoundedCornerShape(20.dp))
            .border(
                1.dp,
                Color.White,
                shape = RoundedCornerShape(20.dp)
            )
            .verticalScroll(rememberScrollState())
    ) {
        var text by remember {
            mutableStateOf("")
        }
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Salvando chave da OpenIA")
            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                },
                label = {
                    Text(text = "OpenIA Key")
                },
                placeholder = {
                    Text(text = "Insira a sua chave da Open IA")
                }
            )
            Box(Modifier.fillMaxWidth()) {
                TextButton(onClick = {
                    onSaveKey(text)
                    onDismissRequest()
                }, Modifier.align(Alignment.CenterEnd)) {
                    Text(text = "Save")
                }
            }
        }

    }
}

@Preview
@Composable
fun OpenAiKeyDialogPreview() {
    LuriTheme {
        Surface {
            OpenAiKeyDialog(
                onSaveKey = { },
                onDismissRequest = {

                }
            )
        }
    }
}