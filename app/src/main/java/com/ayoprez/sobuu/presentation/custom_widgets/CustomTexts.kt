package com.ayoprez.sobuu.presentation.custom_widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ayoprez.sobuu.ui.theme.DarkLava
import com.ayoprez.sobuu.ui.theme.SourceSans
import com.ayoprez.sobuu.ui.theme.WhiteBlue

@Composable
fun IconAndText(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = DarkLava,
    fontSize: TextUnit,
    icon: ImageVector? = null,
    iconPainter: Painter? = null,
    customIcon: @Composable () -> Unit = {},
    iconColor: Color = DarkLava,
    ) {
    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ){
        if(icon == null && iconPainter != null) {
            Icon(
                painter = iconPainter,
                modifier = Modifier.size(16.dp),
                contentDescription = "",
                tint = iconColor,
            )
        } else if(icon != null && iconPainter == null) {
            Icon(
                imageVector = icon,
                modifier = Modifier.size(16.dp),
                contentDescription = "",
                tint = iconColor,
            )
        } else {
            customIcon()
        }

        Text(
            text = text,
            modifier = Modifier.padding(start = 2.dp),
            style = TextStyle(
                fontFamily = SourceSans,
                color = textColor,
                fontSize = fontSize,
            )
        )
    }
}

@Composable
fun Chip(
    text: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontFamily = SourceSans,
                fontSize = 12.sp,
            ),
            color = WhiteBlue,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
        )
    }
}