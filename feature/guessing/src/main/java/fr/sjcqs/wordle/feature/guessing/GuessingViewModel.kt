package fr.sjcqs.wordle.feature.guessing

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.sjcqs.wordle.ui.components.LetterUiState
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
internal class GuessingViewModel @Inject constructor(
) : ViewModel() {
    fun onSubmit(word: String) {
        //TODO("Not yet implemented")
    }

    val uiState: StateFlow<GuessingUiModel> = MutableStateFlow(
        GuessingUiModel(
            GuessUiModel(
                "SMOKE", mapOf(
                    0 to LetterUiState.Correct,
                    1 to LetterUiState.Absent,
                    2 to LetterUiState.Present,
                    3 to LetterUiState.Absent,
                    4 to LetterUiState.Present,
                )
            ),
            GuessUiModel(word = "", isEditable = true),
            GuessUiModel(word = "", isEditable = false),
            GuessUiModel(word = "", isEditable = false),
            GuessUiModel(word = "", isEditable = false),
            GuessUiModel(word = "", isEditable = false),
        )
    )

}

@Immutable
internal data class GuessingUiModel(val guesses: List<GuessUiModel>) {
    constructor(vararg guesses: GuessUiModel) : this(guesses = guesses.toList())
}

@Immutable
internal data class GuessUiModel(
    val word: String,
    val letterState: Map<Int, LetterUiState> = emptyMap(),
    val isEditable: Boolean = false,
)
