package fr.sjcqs.wordle.data.game.assets

interface GameAssetsDataSource {
    fun containsWord(word: String): Boolean
}