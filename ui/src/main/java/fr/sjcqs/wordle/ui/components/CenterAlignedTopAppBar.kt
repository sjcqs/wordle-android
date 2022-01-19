package fr.sjcqs.wordle.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CenterAlignedTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val color by colors.containerColor(scrollFraction = scrollBehavior?.scrollFraction ?: 0f)
    Surface(
        color = color,
        modifier = modifier
    ) {
        CenterAlignedTopAppBar(
            title = title,
            navigationIcon = navigationIcon,
            actions = actions,
            modifier = Modifier.padding(contentPadding)
        )
    }
}