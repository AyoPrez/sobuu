package com.ayoprez.sobuu.presentation.custom_widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithMenu(
    nav: DestinationsNavigator?,
    backgroundColor: Color = WhiteBlue,
    title: String? = null,
    titleColor: Color = DarkLava,
    listItems: List<MenuItemData>,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = modifier
            .fillMaxWidth()
            .composed { modifier },
        title = {
            Text(
                text = title ?: "",
                style = TextStyle(
                    color = titleColor,
                    fontSize = 38.sp,
                    fontFamily = Solway,
                    fontWeight = FontWeight.Medium,
                ),
                textAlign = TextAlign.Center,
            )
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(backgroundColor),
        actions = {
            IconButton(onClick = {
                expanded = true
            }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    tint = DarkLava
                )
            }

            DropdownMenu(
                modifier = Modifier
                    .background(backgroundColor)
                    .width(width = 200.dp),
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                },
                offset = DpOffset(x = (-102).dp, y = (-64).dp),
                properties = PopupProperties()
            ) {

                listItems.forEach { menuItemData ->
                    DropdownMenuItem(
                        onClick = {
                            menuItemData.action()
                            expanded = false
                        },
                        enabled = true,
                        text = {
                            Text(
                                text = menuItemData.text,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = DarkLava
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = menuItemData.icon,
                                contentDescription = menuItemData.text,
                                tint = DarkLava,
                            )
                        }
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                nav?.navigateUp()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "",
                    tint = DarkLava,
                )
            }
        }
    )
}


data class MenuItemData(val text: String, val icon: ImageVector, val action: () -> Unit)

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