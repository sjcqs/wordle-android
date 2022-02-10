package fr.sjcqs.wordle.data.game.db

import fr.sjcqs.wordle.data.game.entity.Game
import fr.sjcqs.wordle.data.game.db.Game as DbGame

internal fun DbGame.fromDb(maxGuesses: Int) = Game(
    word = word,
    expiredAt = expiredAt,
    guesses = guesses,
    isInfinite = isInfinite,
    maxGuesses = maxGuesses
)

internal fun Game.toDb() = DbGame(
    word = word,
    expiredAt = expiredAt,
    guesses = guesses,
    isInfinite = isInfinite,
)