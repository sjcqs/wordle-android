package fr.sjcqs.wordle.feature.game.component

import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.sjcqs.wordle.feature.game.R
import fr.sjcqs.wordle.feature.game.StatsUiModel
import fr.sjcqs.wordle.ui.components.IconButton
import fr.sjcqs.wordle.ui.icons.Icons
import fr.sjcqs.wordle.ui.theme.absent
import fr.sjcqs.wordle.ui.theme.correct
import fr.sjcqs.wordle.ui.theme.onAbsent
import fr.sjcqs.wordle.ui.theme.onCorrect
import java.time.Duration

@Composable
internal fun StatsDialog(stats: StatsUiModel, onDismissRequest: () -> Unit) {
    val context = LocalContext.current
    fun share() {
        stats.sharedText?.let {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, stats.sharedText)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }

    }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {},
        title = {
            val closeOnClickLabel = stringResource(id = R.string.game_stats_label_close)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismissRequest, onClickLabel = closeOnClickLabel) {
                    Icons.Close()
                }
                Text(
                    text = stringResource(R.string.game_statistics),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Stat(labelId = R.string.game_stats_played, value = stats.played)
                    Stat(labelId = R.string.game_stats_win_rate, value = stats.winRate)
                    Stat(labelId = R.string.game_stats_current_streak, value = stats.currentStreak)
                    Stat(labelId = R.string.game_stats_max_streak, value = stats.maxStreak)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .padding(PaddingValues(bottom = 16.dp))
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(R.string.game_performances),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                StatsPerformances(
                    distributions = stats.distributions,
                    wonGames = stats.distributions.values.sum().toFloat()
                )
                if (stats.expiredIn != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.SpaceAround) {
                        Column(
                            modifier = Modifier.semantics(mergeDescendants = true) { },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = stringResource(R.string.game_stats_next_word_in))
                            Text(
                                text = stats.expiredIn.format(),
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        if (stats.sharedText != null) {
                            Button(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .semantics(mergeDescendants = true) { },
                                onClick = { share() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.correct,
                                    contentColor = MaterialTheme.colorScheme.onCorrect,
                                )
                            ) {
                                Icons.Share(modifier = Modifier.clearAndSetSemantics { })
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = stringResource(R.string.game_stats_share))
                            }
                        }
                    }
                }
            }
        }
    )
}

private fun Duration.format(): String {
    return String.format(
        "%d:%02d:%02d",
        toHours(),
        (seconds % (60 * 60)) / 60,
        seconds % 60
    );
}

@Composable
private fun StatsPerformances(distributions: Map<Int, Int>, wonGames: Float) {
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
            .padding(vertical = 2.dp)
            .semantics {
                this.stateDescription = stateDescription
            }) {
            Text(
                modifier = Modifier.clearAndSetSemantics { },
                text = "$guessCount"
            )
            Spacer(modifier = Modifier.width(8.dp))
            val fraction = (count / wonGames).coerceAtLeast(.1f)
            Box(
                modifier = Modifier
                    .run { if (count > 0) fillMaxWidth(fraction) else this }
                    .background(color = MaterialTheme.colorScheme.absent)
                    .padding(horizontal = 2.dp)
            ) {
                Text(
                    color = MaterialTheme.colorScheme.onAbsent,
                    modifier = Modifier
                        .align(
                            if (count > 0) Alignment.CenterEnd else Alignment.Center
                        )
                        .clearAndSetSemantics { },
                    text = "$count"
                )
            }
        }
    }
}

@Composable
private fun Stat(@StringRes labelId: Int, value: String) {
    val label = stringResource(labelId)
    Column(
        modifier = Modifier.semantics(mergeDescendants = true) {
            stateDescription = "$label: $value"
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.clearAndSetSemantics { },
            text = value,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            modifier = Modifier.clearAndSetSemantics { },
            textAlign = TextAlign.Center,
            text = label,
            style = MaterialTheme.typography.labelMedium
        )
    }
}