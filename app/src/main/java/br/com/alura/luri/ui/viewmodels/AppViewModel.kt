package br.com.alura.luri.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.alura.luri.LuriApplication
import br.com.alura.luri.database.OPEN_AI_KEY
import br.com.alura.luri.database.dataStore
import br.com.alura.luri.ui.states.AppUiState
import br.com.alura.luri.ui.states.OpenAiKeyStatus
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "AppViewModel"

@OptIn(BetaOpenAI::class)
class AppViewModel(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState = _uiState.asStateFlow()
    private var job: Job = Job()

    init {
        viewModelScope.launch {
            dataStore.data.collect { pref ->
                pref[OPEN_AI_KEY]?.let { openAiKey ->
                    Log.i(TAG, "Checking the OpenAI API key: $openAiKey")
                    checkOpenAiKeyStatus(openAiKey)
                }
            }
        }
    }

    private suspend fun checkOpenAiKeyStatus(openAiKey: String) {
        _uiState.update { currentState ->
            currentState.copy(
                openAiKeyStatus = try {
                    val openAI = OpenAI(openAiKey)
                    val chatCompletionRequest = ChatCompletionRequest(
                        model = ModelId("gpt-3.5-turbo"),
                        messages = listOf(
                            ChatMessage(
                                role = ChatRole.User,
                                content = "Hello!"
                            )
                        )
                    )
                    openAI.chatCompletion(chatCompletionRequest)
                    OpenAiKeyStatus.VALID
                } catch (e: Exception) {
                    OpenAiKeyStatus.INVALID
                }
            )
        }
    }

    fun openOpenIaDialogConfig() {
        _uiState.update {
            it.copy(isOpenIaDialogOpened = true)
        }
    }

    fun closeOpenIaDialogConfig() {
        _uiState.update {
            it.copy(isOpenIaDialogOpened = false)
        }
    }

    suspend fun saveOpenIaKey(openId: String) {
        dataStore.edit { prefs ->
            prefs[OPEN_AI_KEY] = openId
        }
    }

    fun checkTheOpenAiKey(key: String) {
        job.cancel()
        job = viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(openAiKeyStatus = OpenAiKeyStatus.LOADING)
            }
            delay(2000)
            checkOpenAiKeyStatus(key)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context: Context =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LuriApplication
                AppViewModel(
                    dataStore = context.dataStore
                )
            }
        }
    }

}