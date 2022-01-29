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

    @Composable
    fun Close(
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    ) {
        Icon(
            modifier = modifier,
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = stringResource(id = R.string.content_description_close),
            tint = tint,
        )
    }

    @Composable
    fun Check(
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    ) {
        Icon(
            modifier = modifier,
            painter = painterResource(id = R.drawable.ic_check),
            contentDescription = stringResource(id = R.string.content_description_check),
            tint = tint,
        )
    }

    @Composable
    fun Enter(
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    ) {
        Icon(
            modifier = modifier,
            painter = painterResource(id = R.drawable.ic_return),
            contentDescription = stringResource(id = R.string.content_description_return),
            tint = tint,
        )
    }

    @Composable
    fun Backspace(
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    ) {
        Icon(
            modifier = modifier,
            painter = painterResource(id = R.drawable.ic_backspace),
            contentDescription = stringResource(id = R.string.content_description_backspace),
            tint = tint,
        )
    }

    @Composable
    fun Settings(
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    ) {
        Icon(
            modifier = modifier,
            painter = painterResource(id = R.drawable.ic_settings),
            contentDescription = stringResource(id = R.string.content_description_settings),
            tint = tint,
        )
    }

    @Composable
    fun Share(
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    ) {
        Icon(
            modifier = modifier,
            painter = painterResource(id = R.drawable.ic_share),
            contentDescription = stringResource(id = R.string.content_description_share),
            tint = tint,
        )
    }
}