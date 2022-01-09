package fr.sjcqs.wordle.molecule.viewmodel

import androidx.lifecycle.ViewModel
import app.cash.molecule.AndroidUiDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

abstract class MoleculeViewModel : ViewModel() {
    protected val moleculeScope = CoroutineScope(SupervisorJob() + AndroidUiDispatcher.Main)

    override fun onCleared() {
        super.onCleared()
        moleculeScope.cancel()
    }
}