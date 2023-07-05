package br.com.alura.luri.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alura.luri.models.Message
import br.com.alura.luri.ui.states.ChatUiState
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "ChatViewModel"

class ChatViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    private val openAI = OpenAI("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")

    @OptIn(BetaOpenAI::class)
    private val chatMessages = mutableListOf<ChatMessage>()
    val uiState = _uiState.asStateFlow()

    fun send(text: String) {
        val botMessage = mutableStateOf(
            Message(text = "", false)
        )
        viewModelScope.launch {
            sendToOpenIA(text, onPhraseChange = { newPhrase ->
                botMessage.value = botMessage.value.copy(text = newPhrase)
            })
        }
        _uiState.update { currentState ->
            currentState.copy(
                messages = currentState.messages +
                        mutableStateOf(
                            Message(text = text, isAuthor = true)
                        ) + botMessage
            )
        }
    }

    @OptIn(BetaOpenAI::class)
    private suspend fun sendToOpenIA(
        text: String,
        onPhraseChange: (String) -> Unit,
    ) {
        var phrase = ""
        val request = createRequest(text)
        val chatCompletionChunk = openAI.chatCompletions(request)
        chatCompletionChunk.collect {
            it.choices.forEach { chatChunk ->
                chatChunk.delta?.content?.let { text ->
                    phrase += text
                }
                onPhraseChange(phrase)
            }
        }
    }

    @OptIn(BetaOpenAI::class)
    private fun createRequest(
        text: String,
        modelId: String = "gpt-3.5-turbo"
    ): ChatCompletionRequest {
        chatMessages += ChatMessage(
            role = ChatRole.User,
            content = text
        )
        Log.i(TAG, "createRequest: $chatMessages")
        return ChatCompletionRequest(
            model = ModelId(modelId),
            messages = chatMessages
        )
    }
}

