package fr.sjcqs.wordle.feature.game.model

internal sealed interface GameUiEvent {
    object ClearInput : GameUiEvent
    sealed interface Notify : GameUiEvent
    object InvalidWord : Notify
    object Dismiss : Notify
    data class Share(val text: String) : GameUiEvent
}