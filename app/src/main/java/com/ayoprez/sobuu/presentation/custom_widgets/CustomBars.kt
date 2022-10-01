package com.ayoprez.sobuu.presentation.custom_widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ayoprez.sobuu.presentation.destinations.ProfileScreenDestination
import com.ayoprez.sobuu.ui.theme.DarkLava
import com.ayoprez.sobuu.ui.theme.Solway
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

@Composable
fun TopAppBarWithSearchAndProfile(
    nav: DestinationsNavigator?,
    text: String,
    backgroundColor: Color = WhiteBlue,
    profileIconColor: Color = DarkLava,
    titleColor: Color = DarkLava,
    searchFieldValue: String,
    onSearchFieldValueChange: (String) -> Unit,
    onSearchButtonClick: () -> Unit,
    clearTextButtonClick: () -> Unit,
    onSearchFieldFocusChange: (Boolean) -> Unit,
) {
    Surface(modifier = Modifier
        .background(backgroundColor)
        .fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text,
                style = TextStyle(
                    color = titleColor,
                    fontSize = 38.sp,
                    fontFamily = Solway,
                    fontWeight = FontWeight.Medium,
                ),
                modifier = Modifier
                    .padding(20.dp)
                    .background(backgroundColor),
                textAlign = TextAlign.Center,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp)
            ) {
                SearchBarTextField(
                    searchFieldValue = searchFieldValue,
                    onSearchFieldValueChange = onSearchFieldValueChange,
                    modifier = Modifier
                        .weight(3f),
                    onSearchButtonClick = onSearchButtonClick,
                    clearText = clearTextButtonClick,
                    onSearchFieldFocusChange = onSearchFieldFocusChange,
                )
                IconButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .weight(1f),
                    onClick = {
                        nav?.navigate(ProfileScreenDestination)
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = null,
                        tint = profileIconColor,
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, group = "Done")
@Composable
fun ComposableHomeScreenTopBarPreview() {
    TopAppBarWithSearchAndProfile(
        null,
        "Sobuu",
        searchFieldValue = "",
        onSearchFieldValueChange = {},
        onSearchButtonClick = {},
        clearTextButtonClick = {},
        onSearchFieldFocusChange = {}
    )
}