package com.ayoprez.sobuu.presentation.custom_widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.ayoprez.sobuu.ui.theme.DarkLava
import com.ayoprez.sobuu.ui.theme.SourceSans
import com.ayoprez.sobuu.ui.theme.WhiteBlue
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    nav: DestinationsNavigator?,
    text: String,
    backgroundColor: Color = WhiteBlue,
    iconColor: Color = DarkLava,
    titleColor: Color = DarkLava
) {
    TopAppBar(
        title = {
            Text(
                text,
                style = TextStyle(
                    color = titleColor,
                    fontSize = 24.sp,
                    fontFamily = SourceSans
                )
            )
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(backgroundColor),
        modifier = Modifier.fillMaxWidth(),
        navigationIcon = {
            IconButton(onClick = {
                nav?.navigateUp()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "",
                    tint = iconColor,
                )
            }
        }
    )
}