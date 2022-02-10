package fr.sjcqs.wordle.data.settings.entity

enum class KeyboardLayout {
    Qwerty, Azerty;

    companion object {
        val Default
            get() = Azerty
    }
}