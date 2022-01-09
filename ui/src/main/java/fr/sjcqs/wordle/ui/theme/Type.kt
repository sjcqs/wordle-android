package fr.sjcqs.wordle.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import fr.sjcqs.wordle.ui.R

internal object Fonts {
    val Poppins = FontFamily(
        Font(R.font.poppins_light, FontWeight.Light),
        Font(R.font.poppins_light_italic, FontWeight.Light, FontStyle.Italic),
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_regular_italic, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_medium_italic, FontWeight.Medium, FontStyle.Italic),
        Font(R.font.poppins_semibold, FontWeight.SemiBold),
        Font(R.font.poppins_semibold_italic, FontWeight.SemiBold, FontStyle.Italic),
        Font(R.font.poppins_bold, FontWeight.Bold),
        Font(R.font.poppins_bold_italic, FontWeight.Bold, FontStyle.Italic),
    )

    val Questrial = FontFamily(Font(R.font.questrial_regular, FontWeight.Normal))
}

// Set of Material typography styles to start with
internal val Typography = Typography().run {
    copy(
        displayLarge = displayLarge.copy(fontFamily = Fonts.Poppins),
        displayMedium = displayMedium.copy(fontFamily = Fonts.Poppins),
        displaySmall = displaySmall.copy(fontFamily = Fonts.Poppins),
        headlineLarge = headlineLarge.copy(fontFamily = Fonts.Poppins),
        headlineMedium = headlineMedium.copy(fontFamily = Fonts.Poppins),
        headlineSmall = headlineSmall.copy(fontFamily = Fonts.Poppins),
        titleLarge = titleLarge.copy(fontFamily = Fonts.Poppins),
        titleMedium = titleMedium.copy(fontFamily = Fonts.Poppins),
        titleSmall = titleSmall.copy(fontFamily = Fonts.Poppins),
        bodyLarge = bodyLarge.copy(fontFamily = Fonts.Poppins),
        bodyMedium = bodyMedium.copy(fontFamily = Fonts.Poppins),
        bodySmall = bodySmall.copy(fontFamily = Fonts.Poppins),
        labelLarge = labelLarge.copy(fontFamily = Fonts.Poppins),
        labelMedium = labelMedium.copy(fontFamily = Fonts.Poppins),
        labelSmall = labelSmall.copy(fontFamily = Fonts.Poppins),
    )
}

val Typography.timer: TextStyle
    get() = TextStyle(
        fontFamily = Fonts.Questrial,
        fontWeight = FontWeight.Normal,
        fontSize = 96.sp,
        letterSpacing = (-1.5).sp
    )