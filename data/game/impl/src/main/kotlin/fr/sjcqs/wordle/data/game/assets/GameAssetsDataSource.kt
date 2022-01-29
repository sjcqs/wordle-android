package fr.sjcqs.wordle.data.game.assets

interface GameAssetsDataSource {
    val words: HashSet<String>

    fun containsWord(word: String): Boolean
}