package src.fr.sjcqs.wordle.feature.session.configuration

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun Guessing() {
    val viewModel: GuessingViewModel = hiltViewModel()
    Guessing(viewModel = viewModel)
}

@Composable
private fun Guessing(viewModel: GuessingViewModel) {
    val uiModel by viewModel.uiState.collectAsState()

    Text(text = uiModel.text)
}