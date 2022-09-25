package com.ayoprez.sobuu.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ayoprez.sobuu.R

val SourceSans = FontFamily(
    Font(R.font.source_sans3_regular),
    Font(R.font.source_sans3_bold, FontWeight.Bold),
    Font(R.font.source_sans3_black, FontWeight.Black),
    Font(R.font.source_sans3_light, FontWeight.Light),
    Font(R.font.source_sans3_extra_light, FontWeight.ExtraLight),
    Font(R.font.source_sans3_semibold, FontWeight.SemiBold),
    Font(R.font.source_sans3_it, FontWeight.Normal),
)

val Solway = FontFamily(
    Font(R.font.solway_regular),
    Font(R.font.solway_bold, FontWeight.Bold),
    Font(R.font.solway_light, FontWeight.Light),
    Font(R.font.solway_extra_bold, FontWeight.ExtraBold),
    Font(R.font.solway_medium, FontWeight.Medium),
)


// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = SourceSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Solway,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)