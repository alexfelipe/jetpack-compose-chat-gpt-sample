package br.com.alura.luri

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.alura.luri.ui.components.OpenAiKeyDialog
import br.com.alura.luri.ui.screens.ChatScreen
import br.com.alura.luri.ui.theme.LuriTheme
import br.com.alura.luri.ui.theme.botBackgroundColor
import br.com.alura.luri.ui.viewmodels.AppViewModel
import br.com.alura.luri.ui.viewmodels.ChatViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LuriTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val appViewModel by viewModels<AppViewModel> { AppViewModel.Factory }
                    val chatViewModel by viewModels<ChatViewModel> { ChatViewModel.Factory }
                    val chatUiState by chatViewModel
                        .uiState.collectAsState()
                    val appUiState by appViewModel
                        .uiState.collectAsState()
                    val scope = rememberCoroutineScope()
                    if (appUiState.isOpenIaDialogOpened) {
                        OpenAiKeyDialog(
                            onSaveKeyClick = { key ->
                                scope.launch {
                                    appViewModel.saveOpenIaKey(key)
                                }
                            }, onDismissRequest = {
                                appViewModel.closeOpenIaDialogConfig()
                            }, onGetAKeyClick = {
                                Intent(
                                    Intent.ACTION_VIEW, Uri
                                        .parse("https://platform.openai.com/account/api-keys")
                                ).run {
                                    startActivity(this)
                                }
                            },
                            onKeyValueChange = { key ->
                                appViewModel.checkTheOpenAiKey(key)
                            },
                            openAiKeyStatus = appUiState.openAiKeyStatus
                        )
                    }
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {}, actions = {
                                    Icon(
                                        Icons.Default.Settings,
                                        contentDescription = "Settings icon",
                                        Modifier
                                            .padding(8.dp)
                                            .clip(CircleShape)
                                            .clickable {
                                                appViewModel.openOpenIaDialogConfig()
                                            },
                                        tint = Color.White
                                    )
                                },
                                colors = TopAppBarDefaults
                                    .topAppBarColors(
                                        containerColor = botBackgroundColor
                                    )
                            )
                        }
                    ) {
                        ChatScreen(
                            chatUiState,
                            onSendMessage = { message ->
                                chatViewModel.send(message)
                            },
                            Modifier.padding(it)
                        )
                    }
                }
            }
        }
    }
}