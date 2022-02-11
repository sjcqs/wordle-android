package fr.sjcqs.wordle.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

public fun <T> Flow<T>.collectLatestIn(scope: CoroutineScope, action: suspend (value: T) -> Unit) {
    scope.launch { collectLatest(action) }
}