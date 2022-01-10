package fr.sjcqs.wordle.feature.guessing

import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.derivedWindowInsetsTypeOf
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.insets.ui.Scaffold
import fr.sjcqs.wordle.ui.components.Word


@Composable
fun Guessing() {
    val viewModel: GuessingViewModel = hiltViewModel()
    val ime = LocalWindowInsets.current.ime
    val elevation: Dp by remember { derivedStateOf { if (ime.isVisible) 4.dp else 0.dp } }
    Scaffold(
        topBar = {
            Surface(
                shadowElevation = elevation,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(vertical = 16.dp)
                ) {
                    Text(
                        text = "Le Mot",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.headlineLarge,
                    )
                }
            }
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                Guessing(viewModel = viewModel)
            }
        })
}

@Composable
private fun Guessing(viewModel: GuessingViewModel) {
    val uiModel by viewModel.uiState.collectAsState()
    Guessing(
        guesses = uiModel.guesses,
        onSubmit = { viewModel.onSubmit(it) }
    )
}

@Composable
private fun Guessing(
    guesses: List<GuessUiModel>,
    onSubmit: (word: String) -> Unit
) {
    val ime = LocalWindowInsets.current.ime
    val navBars = LocalWindowInsets.current.navigationBars
    val insets = remember(ime, navBars) { derivedWindowInsetsTypeOf(ime, navBars) }
    val navigationWithImeBottom = with(LocalDensity.current) { insets.bottom.toDp() }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState, enabled = false)
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        guesses.forEachIndexed { index, guess ->
            Guess(
                scrollState = scrollState,
                guess = guess,
                bottomSpacing = navigationWithImeBottom,
                onSubmit = onSubmit,
                isImeVisible = ime.isVisible
            )
            if (index < guesses.lastIndex) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Spacer(modifier = Modifier.height(navigationWithImeBottom))
    }
}

@Composable
private fun Guess(
    guess: GuessUiModel,
    scrollState: ScrollState,
    bottomSpacing: Dp,
    onSubmit: (word: String) -> Unit,
    isImeVisible: Boolean
) {
    val density = LocalDensity.current
    var scrollBy: Dp by remember { mutableStateOf(0.dp) }
    LaunchedEffect(key1 = scrollBy) {
        if (isImeVisible) {
            scrollState.scrollBy(scrollBy.value)
        }
    }
    val context = LocalContext.current
    Word(
        modifier = if (guess.isEditable) {
            Modifier.onGloballyPositioned { coordinates ->
                val parentHeight = with(density) {
                    (coordinates.parentCoordinates?.size?.height ?: 0).toDp()
                }
                val bottom = with(density) { coordinates.positionInWindow().y.toDp() }
                val offset = bottom - (parentHeight - bottomSpacing)
                if (offset > 0.dp) {
                    scrollBy = offset + 16.dp
                }
            }
        } else Modifier,
        value = guess.word,
        onSubmit = { word ->
            if (guess.isEditable) {
                Toast.makeText(context, "Submit: $word", Toast.LENGTH_SHORT).show()
                onSubmit(word)
            }
        },
        isEditable = guess.isEditable,
        letterStates = guess.letterState
    )
}
