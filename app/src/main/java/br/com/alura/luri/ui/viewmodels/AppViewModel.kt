package br.com.alura.luri.ui.viewmodels

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.alura.luri.LuriApplication
import br.com.alura.luri.database.OPEN_AI_KEY
import br.com.alura.luri.database.dataStore
import br.com.alura.luri.ui.states.AppUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState = _uiState.asStateFlow()

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