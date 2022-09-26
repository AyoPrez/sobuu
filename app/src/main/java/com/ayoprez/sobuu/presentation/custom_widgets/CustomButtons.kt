package com.ayoprez.sobuu.presentation.custom_widgets

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.ayoprez.sobuu.ui.theme.SourceSans
import com.ayoprez.sobuu.ui.theme.SpanishGray
import com.ayoprez.sobuu.ui.theme.Vermilion
import com.ayoprez.sobuu.ui.theme.WhiteBlue

@Composable
fun RoundedFillButton(
    onClick: () -> Unit,
    color: Color = Vermilion,
    text: String,
) {
    FilledTonalButton(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = WhiteBlue,
            disabledContainerColor = SpanishGray,
            disabledContentColor = WhiteBlue,
        )
    ) {
        Text(
            text,
            style = TextStyle(
                fontSize = 22.sp,
                color = WhiteBlue,
                fontWeight = FontWeight.Normal,
                fontFamily = SourceSans
            ),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun CustomTextButton(
    onClick: () -> Unit,
    color: Color = WhiteBlue,
    text: String,
    fontSize: TextUnit = 12.sp,
) {
    TextButton(
        onClick = onClick
    ) {
        Text(
            text,
            style = TextStyle(
                fontSize = fontSize,
                fontFamily = SourceSans,
                color = color
            ),
        )
    }
}

@Composable
fun CustomUnderlineTextButton(
    onClick: () -> Unit,
    color: Color = WhiteBlue,
    text: String,
    fontSize: TextUnit = 12.sp,
) {
    TextButton(
        onClick = onClick
    ) {
        Text(
            text,
            style = TextStyle(
                fontSize = fontSize,
                fontFamily = SourceSans,
                color = color
            ),
            textDecoration = TextDecoration.Underline,
            textAlign = TextAlign.Center,
        )
    }
}