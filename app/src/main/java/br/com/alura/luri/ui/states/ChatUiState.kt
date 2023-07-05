package br.com.alura.luri.ui.states

import androidx.compose.runtime.State
import br.com.alura.luri.models.Message

data class ChatUiState(
    val messages: List<State<Message>> = emptyList()
)
