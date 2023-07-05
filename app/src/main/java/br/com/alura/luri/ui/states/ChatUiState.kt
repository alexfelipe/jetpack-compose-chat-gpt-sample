package br.com.alura.luri.ui.states

import br.com.alura.luri.models.Message
import kotlinx.coroutines.flow.StateFlow

data class ChatUiState(
    val messages: List<StateFlow<Message?>> = emptyList()
)
