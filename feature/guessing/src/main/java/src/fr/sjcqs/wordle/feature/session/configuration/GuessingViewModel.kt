package src.fr.sjcqs.wordle.feature.session.configuration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import app.cash.molecule.launchMolecule
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.sjcqs.wordle.molecule.viewmodel.MoleculeViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
internal class GuessingViewModel @Inject constructor(
) : MoleculeViewModel() {
    val uiState: StateFlow<SessionConfigurationUiModel> = moleculeScope.launchMolecule {
        sessionConfigurationState("")
    }

    @Composable
    private fun sessionConfigurationState(
        text: String
    ): SessionConfigurationUiModel {
        return SessionConfigurationUiModel(text = text)
    }

}

@Immutable
internal data class SessionConfigurationUiModel(
    val text: String
)
