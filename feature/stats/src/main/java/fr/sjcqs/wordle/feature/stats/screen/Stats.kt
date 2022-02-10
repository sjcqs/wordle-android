package fr.sjcqs.wordle.feature.stats.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.Scaffold
import fr.sjcqs.wordle.feature.stats.R
import fr.sjcqs.wordle.feature.stats.StatsViewModel
import fr.sjcqs.wordle.feature.stats.model.StatsUiModel
import fr.sjcqs.wordle.ui.components.CenterAlignedTopAppBar
import fr.sjcqs.wordle.ui.components.Divider
import fr.sjcqs.wordle.ui.components.IconButton
import fr.sjcqs.wordle.ui.icons.Icons
import fr.sjcqs.wordle.ui.theme.absent
import fr.sjcqs.wordle.ui.theme.correct
import fr.sjcqs.wordle.ui.theme.onAbsent

@Composable
fun Stats(onClose: () -> Unit) {
    val viewModel: StatsViewModel = hiltViewModel()

    val stats by viewModel.statsFlow.collectAsState()

    Scaffold(
        topBar = {
            val closeOnClickLabel = stringResource(id = R.string.stats_label_close)
            CenterAlignedTopAppBar(
                contentPadding = rememberInsetsPaddingValues(
                    insets = LocalWindowInsets.current.statusBars
                ),
                shadowElevation = 4.dp,
                navigationIcon = {
                    IconButton(
                        onClick = onClose,
                        onClickLabel = closeOnClickLabel
                    ) {
                        Icons.Close()
                    }
                },
                title = { Text(text = stringResource(R.string.stats_title)) },
            )
        },
        content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Stats(stats = stats, modifier = Modifier.fillMaxSize())
            }
        }
    )
}

@Composable
private fun Stats(stats: StatsUiModel, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Stat(labelId = R.string.stats_played, value = stats.played)
            Stat(labelId = R.string.stats_win_rate, value = stats.winRate)
            Stat(labelId = R.string.stats_current_streak, value = stats.currentStreak)
            Stat(labelId = R.string.stats_max_streak, value = stats.maxStreak)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Divider()
        Spacer(modifier = Modifier.height(24.dp))
        StatsPerformances(
            modifier = Modifier,
            distributions = stats.distributions,
            wonGames = stats.distributions.values.sum().toFloat()
        )
        Spacer(modifier = Modifier.navigationBarsHeight())
    }
}

@Composable
private fun StatsPerformances(
    modifier: Modifier,
    distributions: Map<Int, Int>,
    wonGames: Float
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .padding(PaddingValues(bottom = 12.dp))
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                modifier = Modifier.semantics { heading() },
                text = stringResource(R.string.stats_performances_header),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        distributions.forEach { (guessCount, count) ->

            val stateDescription = if (count == 0) {
                stringResource(id = R.string.game_stats_performance_description_none, guessCount)
            } else {
                LocalContext.current.resources.getQuantityString(
                    R.plurals.game_stats_performance_description,
                    count,
                    guessCount,
                    count
                )
            }
            Row(modifier = Modifier
                .padding(vertical = 4.dp)
                .clearAndSetSemantics { this.stateDescription = stateDescription }
            ) {
                Text(
                    text = "$guessCount",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.width(8.dp))
                val fraction = (count / wonGames).coerceAtLeast(.1f)
                val color = if (count > 0) {
                    MaterialTheme.colorScheme.correct
                } else MaterialTheme.colorScheme.absent
                Box(
                    modifier = Modifier
                        .run { if (count > 0) fillMaxWidth(fraction) else this }
                        .background(color = color)
                        .padding(horizontal = 4.dp)
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.onAbsent,
                        modifier = Modifier
                            .align(if (count > 0) Alignment.CenterEnd else Alignment.Center),
                        text = "$count",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun Stat(@StringRes labelId: Int, value: String) {
    val label = stringResource(labelId)
    Column(
        modifier = Modifier.clearAndSetSemantics {
            stateDescription = "$label: $value"
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            textAlign = TextAlign.Center,
            text = label,
            style = MaterialTheme.typography.labelLarge
        )
    }
}