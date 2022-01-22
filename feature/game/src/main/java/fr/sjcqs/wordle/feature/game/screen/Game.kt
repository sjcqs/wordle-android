package fr.sjcqs.wordle.feature.game.screen

import android.widget.Space
import androidx.annotation.StringRes
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.Scaffold
import fr.sjcqs.wordle.feature.game.GameUiEvent
import fr.sjcqs.wordle.feature.game.GameUiState
import fr.sjcqs.wordle.feature.game.GameViewModel
import fr.sjcqs.wordle.feature.game.R
import fr.sjcqs.wordle.feature.game.SpaceBetweenGuesses
import fr.sjcqs.wordle.feature.game.StatsUiModel
import fr.sjcqs.wordle.feature.game.component.Guessing
import fr.sjcqs.wordle.ui.components.CenterAlignedTopAppBar
import fr.sjcqs.wordle.ui.components.Word
import fr.sjcqs.wordle.ui.icons.Icons
import fr.sjcqs.wordle.ui.theme.absent
import fr.sjcqs.wordle.ui.theme.correct
import fr.sjcqs.wordle.ui.theme.onAbsent
import fr.sjcqs.wordle.ui.theme.onCorrect
import java.time.Duration
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun Game() {
    val invalidWord = stringResource(id = R.string.guessing_invalid_word)

    val viewModel: GameViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val ime = LocalWindowInsets.current.ime

    val stats by viewModel.stats.collectAsState()

    val scaffoldState = rememberScaffoldState()
    val snackbarState by derivedStateOf { scaffoldState.snackbarHostState }
    val coroutineScope = rememberCoroutineScope()

    val keyboard = LocalSoftwareKeyboardController.current

    val closeKeyboard = {
        keyboard?.hide()
    }

    fun snackbar(message: String) {
        coroutineScope.launch {
            snackbarState.showSnackbar(message)
        }
    }

    val (typingWord, setTypingWord) = remember(viewModel) {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                GameUiEvent.ClearInput -> setTypingWord("")
                GameUiEvent.InvalidWord -> snackbar(invalidWord)
                GameUiEvent.Dismiss -> snackbarState.currentSnackbarData?.dismiss()
                GameUiEvent.CloseKeyboard -> closeKeyboard()
            }
        }
    }

    var showStatsDialog by remember { mutableStateOf(false) }
    if (showStatsDialog) {
        StatsDialog(
            stats = stats,
            onDismissRequest = { showStatsDialog = false }
        )
    }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(
                hostState = it,
                modifier = Modifier.navigationBarsWithImePadding()
            )
        },
        topBar = {
            CenterAlignedTopAppBar(
                contentPadding = rememberInsetsPaddingValues(
                    insets = LocalWindowInsets.current.statusBars
                ),
                title = { Text(text = stringResource(id = R.string.game_title)) },
                actions = {
                    IconButton(onClick = { showStatsDialog = true }) {
                        Icons.Stats()
                    }
                }
            )
        },
        content = { paddingValues ->
            val scrollState = rememberScrollState()
            Surface(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(scrollState, enabled = ime.isVisible)
            ) {
                Game(
                    state = uiState,
                    typingWord = typingWord,
                    onValueChanged = setTypingWord,
                    scrollState = scrollState
                )
            }
        })
}

@Composable
private fun StatsDialog(stats: StatsUiModel, onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {},
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismissRequest) { Icons.Close() }
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
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Prochain mot dans:")
                            Text(
                                text = stats.expiredIn.format(),
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            onClick = { /*TODO*/ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.correct,
                                contentColor = MaterialTheme.colorScheme.onCorrect,
                            )
                        ) {
                            Icons.Share()
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = stringResource(R.string.game_stats_share))
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
        toMinutesPart(),
        toSecondsPart()
    );
}

@Composable
private fun StatsPerformances(distributions: Map<Int, Int>, wonGames: Float) {
    distributions.forEach { (guessCount, count) ->
        Row(modifier = Modifier.padding(vertical = 2.dp)) {
            Text(text = "$guessCount")
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
                    modifier = Modifier.align(
                        if (count > 0) Alignment.CenterEnd else Alignment.Center
                    ),
                    text = "$count"
                )
            }
        }
    }
}

@Composable
private fun <T : Any> Stat(@StringRes labelId: Int, value: T) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "$value", style = MaterialTheme.typography.titleLarge)
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(labelId),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun Game(
    state: GameUiState,
    typingWord: String,
    onValueChanged: (String) -> Unit,
    scrollState: ScrollState,
) {
    when (state) {
        is GameUiState.Guessing -> Guessing(
            uiState = state,
            value = typingWord,
            onValueChanged = onValueChanged,
            scrollState = scrollState,
        )
        is GameUiState.Loading -> Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            repeat(state.maxGuesses) { index ->
                Word()
                if (index != state.maxGuesses - 1) {
                    Spacer(modifier = Modifier.height(SpaceBetweenGuesses))
                }
            }
        }
    }
}

