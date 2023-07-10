package br.com.alura.luri.ui.states

data class AppUiState(
    val isOpenIaDialogOpened: Boolean = false,
    val openAiKeyStatus: OpenAiKeyStatus = OpenAiKeyStatus.DEFAULT
)

sealed class OpenAiKeyStatus {
    object VALID : OpenAiKeyStatus()
    object INVALID : OpenAiKeyStatus()
    object DEFAULT : OpenAiKeyStatus()
    object LOADING : OpenAiKeyStatus()
}