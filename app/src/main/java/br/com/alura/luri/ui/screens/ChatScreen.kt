package br.com.alura.luri.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import br.com.alura.luri.models.Message
import br.com.alura.luri.ui.components.ChatMessage
import br.com.alura.luri.ui.states.ChatError
import br.com.alura.luri.ui.states.ChatUiState
import br.com.alura.luri.ui.theme.LuriTheme
import br.com.alura.luri.ui.theme.authorBackgroundColor
import com.aallam.openai.api.BetaOpenAI
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatScreen(
    uiState: ChatUiState,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var userInput by remember {
        mutableStateOf("")
    }
    val lazyListState = rememberLazyListState()
    val messages = uiState.messages
    Column(
        modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(Modifier.weight(1f)) {
            LazyColumn(
                Modifier.fillMaxSize(),
                lazyListState
            ) {
                items(messages) { messageState ->
                    val message = messageState.value
                    LaunchedEffect(message) {
                        if (messages.lastIndex > -1) {
                            lazyListState.animateScrollToItem(
                                messages.lastIndex + 1
                            )
                        }
                    }
                    ChatMessage(
                        text = message.text,
                        isAuthor = message.isAuthor
                    )
                }
            }
            uiState.error?.let {
                Box(
                    Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Color.Gray,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(8.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Text(text = uiState.error.message)
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = userInput,
                onValueChange = {
                    userInput = it
                },
                Modifier
                    .padding(8.dp)
                    .weight(1f),
                placeholder = {
                    Text(text = "Digite sua mensagem...")
                },
                shape = RoundedCornerShape(20.dp),
            )
            val keyboardController = LocalSoftwareKeyboardController.current
            val scope = rememberCoroutineScope()
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(CircleShape)
                    .size(50.dp)
                    .clickable {
                        scope.launch {
                            keyboardController?.hide()
                            delay(100)
                            onSendMessage(userInput)
                            userInput = ""
                        }
                    }
                    .fillMaxHeight()
                    .background(
                        authorBackgroundColor,
                        shape = CircleShape
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    Icons.Default.Send, contentDescription = "send icon",
                    Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Preview
@Composable
fun ChatScreenWithErrorPreview() {
    LuriTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            ChatScreen(uiState = ChatUiState(
                messages = List(15) {
                    mutableStateOf(
                        Message(
                            LoremIpsum(Random.nextInt(2, 20)).values
                                .first(),
                            isAuthor = it % 2 == 0
                        )
                    )
                },
                error = ChatError("a error message to display", Exception())
            ), onSendMessage = {})
        }
    }
}
