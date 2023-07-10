package br.com.alura.luri.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import br.com.alura.luri.ui.states.OpenAiKeyStatus
import br.com.alura.luri.ui.theme.LuriTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenAiKeyDialog(
    onSaveKeyClick: (String) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    openAiKeyStatus: OpenAiKeyStatus = OpenAiKeyStatus.DEFAULT,
    onKeyValueChange: (String) -> Unit = {},
    onGetAKeyClick: () -> Unit = {}
) {

    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier
            .heightIn(min = 200.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .border(
                1.dp,
                Color.White,
                shape = RoundedCornerShape(20.dp)
            )
            .background(MaterialTheme.colorScheme.background)
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
                    onKeyValueChange(it)
                },
                label = {
                    Text(text = "OpenIA Key")
                },
                placeholder = {
                    Text(text = "Insira a sua chave da Open IA")
                },
            )
            when (openAiKeyStatus) {
                OpenAiKeyStatus.DEFAULT -> {}
                OpenAiKeyStatus.INVALID -> Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Chave inválida",
                        color = Color.Red
                    )
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "ícone de x indicando chave inválida",
                        tint = Color.Red
                    )
                }

                OpenAiKeyStatus.VALID -> Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "Chave válida",
                        color = Color.Green
                    )
                    Icon(
                        Icons.Default.Done,
                        contentDescription = "ícone feito indicando chave válida",
                        tint = Color.Green
                    )
                }

                OpenAiKeyStatus.LOADING -> Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Verificando Chave"
                    )
                    Box(modifier = Modifier.size(20.dp)) {
                        CircularProgressIndicator(strokeWidth = 2.dp)
                    }
                }
            }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    onGetAKeyClick()
                }, Modifier) {
                    Text(text = "Obter uma chave")
                }
                TextButton(onClick = {
                    onSaveKeyClick(text)
                    onDismissRequest()
                }, Modifier) {
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
                onSaveKeyClick = { },
                onGetAKeyClick = {},
                onDismissRequest = {

                }
            )
        }
    }
}

@Preview
@Composable
fun OpenAiKeyDialogWithInvalidOpenAIKeyPreview() {
    LuriTheme {
        Surface {
            OpenAiKeyDialog(
                onSaveKeyClick = { },
                onGetAKeyClick = {},
                onDismissRequest = {

                },
                openAiKeyStatus = OpenAiKeyStatus.INVALID
            )
        }
    }
}

@Preview
@Composable
fun OpenAiKeyDialogWithValidOpenAIKeyPreview() {
    LuriTheme {
        Surface {
            OpenAiKeyDialog(
                onSaveKeyClick = { },
                onGetAKeyClick = {},
                onDismissRequest = {

                },
                openAiKeyStatus = OpenAiKeyStatus.VALID
            )
        }
    }
}

@Preview
@Composable
fun OpenAiKeyDialogLoadingOpenAIKeyPreview() {
    LuriTheme {
        Surface {
            OpenAiKeyDialog(
                onSaveKeyClick = { },
                onGetAKeyClick = {},
                onDismissRequest = {

                },
                openAiKeyStatus = OpenAiKeyStatus.LOADING
            )
        }
    }
}