package fr.sjcqs.wordle.feature.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.sjcqs.wordle.data.game.GameRepository
import fr.sjcqs.wordle.feature.stats.model.StatsUiModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
internal class StatsViewModel @Inject constructor(gameRepository: GameRepository) : ViewModel() {

    val statsFlow: StateFlow<StatsUiModel> = gameRepository.statsFlow
        .map { stats -> stats.toUiModel() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, StatsUiModel())

}

