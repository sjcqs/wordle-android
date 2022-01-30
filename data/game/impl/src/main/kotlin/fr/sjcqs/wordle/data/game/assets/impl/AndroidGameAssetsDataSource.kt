package fr.sjcqs.wordle.data.game.assets.impl

import android.content.Context
import fr.sjcqs.wordle.annotations.ApplicationContext
import fr.sjcqs.wordle.data.game.assets.GameAssetsDataSource
import javax.inject.Inject

class AndroidGameAssetsDataSource @Inject constructor(
    @ApplicationContext
    private val context: Context,
) : GameAssetsDataSource {
    private val words: HashSet<String>
        get() {
            return context.assets.open("words.txt")
                .bufferedReader()
                .readLines()
                .toHashSet()
        }

    override fun containsWord(word: String) = words.contains(word)

}