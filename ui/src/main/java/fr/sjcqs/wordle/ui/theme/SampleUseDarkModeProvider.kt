package fr.sjcqs.wordle.ui.theme

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class SampleUseDarkModeProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(true, false)
}