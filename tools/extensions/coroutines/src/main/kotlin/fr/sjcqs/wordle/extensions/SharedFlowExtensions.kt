package fr.sjcqs.wordle.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

fun <T> MutableSharedFlow<T>.emitIn(scope: CoroutineScope, value: T) = scope.launch { emit(value) }