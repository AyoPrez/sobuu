package com.ayoprez.sobuu.presentation.custom_widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
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
import com.ayoprez.sobuu.ui.theme.DarkLava
import com.ayoprez.sobuu.ui.theme.SourceSans

@Composable
fun IconAndText(
    text: String,
    textColor: Color = DarkLava,
    fontSize: TextUnit,
    icon: ImageVector? = null,
    iconPainter: Painter? = null,
    customIcon: @Composable () -> Unit = {},
    iconColor: Color = DarkLava,
    modifier: Modifier = Modifier,
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