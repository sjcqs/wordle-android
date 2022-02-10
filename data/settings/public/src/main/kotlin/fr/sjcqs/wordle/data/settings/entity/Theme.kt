package fr.sjcqs.wordle.data.settings.entity

enum class Theme {
    Dark, Light, System;

    companion object {
        val Default
            get() = System
    }
}