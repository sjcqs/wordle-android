package fr.sjcqs.wordle.data.settings.entity

data class Settings(
    val keyboardLayout: KeyboardLayout = KeyboardLayout.Default,
    val theme: Theme = Theme.Default,
    val gameMode: GameMode = GameMode.Default,
)