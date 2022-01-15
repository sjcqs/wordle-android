package fr.sjcqs.wordle.data.game.db

import fr.sjcqs.wordle.data.game.entity.Game
import fr.sjcqs.wordle.data.game.db.Game as DbGame

internal fun DbGame.fromDb(maxGuesses: Int): Game {
    return Game(
        word = word,
        expiredAt = expiredAt,
        guesses = guesses,
        isFinished = isFinished,
        guessesCount = maxGuesses
    )
}

internal fun Game.toDb(): DbGame {
    return DbGame(
        word = word,
        expiredAt = expiredAt,
        guesses = guesses,
        isFinished = isFinished
    )
}