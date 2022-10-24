package com.ayoprez.sobuu.presentation.custom_widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.presentation.destinations.HomeScreenDestination
import com.ayoprez.sobuu.presentation.destinations.ProfileScreenDestination
import com.ayoprez.sobuu.presentation.destinations.ShelvesScreenDestination
import com.ayoprez.sobuu.ui.theme.*
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    nav: DestinationsNavigator?,
    text: String,
    backgroundColor: Color = WhiteBlue,
    titleSize: TextUnit = 24.sp,
    iconColor: Color = DarkLava,
    titleColor: Color = DarkLava
) {
    TopAppBar(
        title = {
            Text(
                text,
                style = TextStyle(
                    color = titleColor,
                    fontSize = titleSize,
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
    Surface(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
    ) {
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
fun SimpleTransparentTopAppBar(
    modifier: Modifier = Modifier,
    nav: DestinationsNavigator?,
    backgroundColor: Color = WhiteBlue.copy(alpha = 0f),
) {
    TopAppBar(
        modifier = modifier
            .fillMaxWidth()
            .then(modifier),
        title = {
            Text("")
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(backgroundColor),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithMenu(
    modifier: Modifier = Modifier,
    nav: DestinationsNavigator?,
    backgroundColor: Color = WhiteBlue,
    title: String? = null,
    titleSize: TextUnit = 38.sp,
    titleColor: Color = DarkLava,
    listItems: List<MenuItemData>,
    showCollapseMenu: Boolean = true,
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = modifier
            .fillMaxWidth()
            .then(modifier),
        title = {
            Text(
                text = title ?: "",
                style = TextStyle(
                    color = titleColor,
                    fontSize = titleSize,
                    fontFamily = Solway,
                    fontWeight = FontWeight.Medium,
                ),
                textAlign = TextAlign.Center,
            )
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(backgroundColor),
        actions = {
            if (showCollapseMenu) {
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
                        if(!menuItemData.displayIt) {
                            return@forEach
                        }
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
            } else {
                val menuItem = listItems[0]

                IconButton(
                    onClick = {
                        menuItem.action.invoke()
                    },
                ) {
                    Icon(
                        imageVector = menuItem.icon,
                        contentDescription = menuItem.text,
                        tint = DarkLava,
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


@Composable
fun CustomBottomAppBar(
    modifier: Modifier = Modifier,
    nav: DestinationsNavigator? = null,
    displayLabels: Boolean = true,
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth(),
        containerColor = GreenSheen,
    ) {
        var currentRoute by remember { mutableStateOf(HomeScreenDestination.route) }

        val config = LocalConfiguration.current
        val screenWidth = config.screenWidthDp.dp
        val itemWidth = screenWidth / 4

        Column(
            modifier = Modifier
                .background(if (currentRoute == HomeScreenDestination.route) Vermilion else GreenSheen)
                .width(itemWidth)
                .fillMaxHeight()
                .clickable {
                    nav?.navigate(HomeScreenDestination)
                    currentRoute = HomeScreenDestination.route
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Book,
                contentDescription = "",
                modifier = Modifier
                    .width(36.dp)
                    .height(36.dp),
                tint = if (currentRoute == HomeScreenDestination.route) DarkLava else WhiteBlue
            )
            if(displayLabels) {
                Text(
                    text = stringResource(id = R.string.bottom_menu_books),
                    style = TextStyle(
                        fontFamily = SourceSans,
                        fontSize = if (currentRoute == HomeScreenDestination.route) 16.sp else 12.sp,
                        color = if (currentRoute == HomeScreenDestination.route) DarkLava else WhiteBlue,
                        fontWeight = if (currentRoute == HomeScreenDestination.route) FontWeight.Medium else FontWeight.Normal,
                    )
                )
            }
        }

        Column(
            modifier = Modifier
                .background(if (currentRoute == ShelvesScreenDestination.route) Vermilion else GreenSheen)
                .width(itemWidth)
                .fillMaxHeight()
                .clickable {
                    nav?.navigate(ShelvesScreenDestination)
                    currentRoute = ShelvesScreenDestination.route
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_bookshelf),
                contentDescription = "",
                modifier = Modifier
                    .width(36.dp)
                    .height(36.dp),
                tint = if (currentRoute == ShelvesScreenDestination.route) DarkLava else WhiteBlue
            )
            if(displayLabels) {
                Text(
                    text = stringResource(id = R.string.bottom_menu_shelves),
                    style = TextStyle(
                        fontFamily = SourceSans,
                        fontSize = if (currentRoute == ShelvesScreenDestination.route) 16.sp else 12.sp,
                        color = if (currentRoute == ShelvesScreenDestination.route) DarkLava else WhiteBlue,
                        fontWeight = if (currentRoute == ShelvesScreenDestination.route) FontWeight.Medium else FontWeight.Normal,
                    )
                )
            }
        }

        Column(
            modifier = Modifier
                .background(GreenSheen)
                .width(itemWidth)
                .fillMaxHeight()
                .clickable {
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Groups,
                contentDescription = "",
                modifier = Modifier
                    .width(36.dp)
                    .height(36.dp),
                tint = WhiteBlue
            )
            if(displayLabels) {
                Text(
                    text = stringResource(id = R.string.bottom_menu_friends),
                    style = TextStyle(
                        fontFamily = SourceSans,
                        fontSize = 12.sp,
                        color = WhiteBlue,
                    )
                )
            }
        }

        Column(
            modifier = Modifier
                .background(GreenSheen)
                .width(itemWidth)
                .fillMaxHeight()
                .clickable {
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_trophy),
                contentDescription = "",
                modifier = Modifier
                    .width(36.dp)
                    .height(36.dp),
                tint = WhiteBlue
            )

            if(displayLabels) {
                Text(
                    text = stringResource(id = R.string.bottom_menu_challenges),
                    style = TextStyle(
                        fontFamily = SourceSans,
                        fontSize = 12.sp,
                        color = WhiteBlue,
                    )
                )
            }
        }
    }
}

data class MenuItemData(val text: String, val icon: ImageVector, val action: () -> Unit, val displayIt: Boolean = true)

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