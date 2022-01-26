package fr.sjcqs.wordle.feature.stats.model

internal sealed interface StatsUiEvent {
    data class Share(val text: String) : StatsUiEvent
}