package br.com.alura.luri.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.alura.luri.LuriApplication
import br.com.alura.luri.database.OPEN_AI_KEY
import br.com.alura.luri.database.dataStore
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

class ChatViewModel(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    private var openAI: OpenAI? = null

    @OptIn(BetaOpenAI::class)
    private val chatMessages = mutableListOf<ChatMessage>()
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            dataStore.data.collect { pref ->
                pref[OPEN_AI_KEY]?.let { openIaKey ->
                    Log.i(TAG, "data collecting: $openIaKey")
                    openAI = OpenAI(openIaKey)
                }
            }
        }
    }



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
        openAI?.let {
            val chatCompletionChunk = it.chatCompletions(request)
            chatCompletionChunk.collect { chatCompletionChunk ->
                chatCompletionChunk.choices.forEach { chatChunk ->
                    chatChunk.delta?.content?.let { text ->
                        phrase += text
                    }
                    onPhraseChange(phrase)
                }
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
        return ChatCompletionRequest(
            model = ModelId(modelId),
            messages = chatMessages
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context: Context = this[APPLICATION_KEY] as LuriApplication
                ChatViewModel(
                    dataStore = context.dataStore
                )
            }
        }

    }

}

