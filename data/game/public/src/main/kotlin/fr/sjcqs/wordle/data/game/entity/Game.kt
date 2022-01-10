package fr.sjcqs.wordle.data.game.entity

data class Game(
    val world: String,
    val guesses: List<Guess>,
    val state: State,
) {
    sealed interface State {
        object Playing : State
        data class Finished(val isWin: Boolean) : State
    }
}