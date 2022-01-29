package fr.sjcqs.wordle.feature.settings.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import fr.sjcqs.wordle.ui.icons.Icons

internal val ChipShape = RoundedCornerShape(50)

@Composable
internal fun Chip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val selectedColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.background
        }
    )
    val chipModifier = if (isSelected) {
        Modifier.background(color = selectedColor, shape = ChipShape)
    } else {
        Modifier.border(1.dp, MaterialTheme.colorScheme.tertiary, ChipShape)
    }
    Box(
        modifier = chipModifier
            .clip(ChipShape)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .animateContentSize()

    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            var height by remember { mutableStateOf(0.dp) }
            val density = LocalDensity.current
            val contentColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onBackground
            }
            AnimatedVisibility(visible = isSelected) {
                Icons.Check(modifier = Modifier.height(height), tint = contentColor)
            }
            if (isSelected) {
                Spacer(modifier = Modifier.width(2.dp))
            }
            Text(
                modifier = Modifier.onGloballyPositioned {
                    height = with(density) { it.boundsInParent().height.toDp() }
                },
                text = text,
                color = contentColor
            )
        }
    }
}