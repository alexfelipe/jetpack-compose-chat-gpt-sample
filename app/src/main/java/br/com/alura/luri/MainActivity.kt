package br.com.alura.luri

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.alura.luri.ui.screens.ChatScreen
import br.com.alura.luri.ui.states.ChatUiState
import br.com.alura.luri.ui.theme.LuriTheme
import br.com.alura.luri.ui.viewmodels.ChatViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LuriTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel by viewModels<ChatViewModel>()
                    val uiState by viewModel.uiState
                        .collectAsState(initial = ChatUiState())
                    ChatScreen(uiState, onSendMessage = {
                        viewModel.send(it)
                    })
                }
            }
        }
    }
}