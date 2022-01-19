package fr.sjcqs.wordle.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import fr.sjcqs.wordle.ui.R

object Icons {
    @Composable
    fun Stats(
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    ) {
        Icon(
            modifier = modifier,
            painter = painterResource(id = R.drawable.ic_chart),
            contentDescription = stringResource(id = R.string.content_description_stats),
            tint = tint,
        )
    }
}