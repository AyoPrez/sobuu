package com.ayoprez.sobuu.presentation.main

import android.app.Activity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.presentation.authentication.login.LoginViewModel
import com.ayoprez.sobuu.presentation.custom_widgets.TopAppBarWithSearchAndProfile
import com.ayoprez.sobuu.presentation.destinations.HomeScreenDestination
import com.ayoprez.sobuu.presentation.destinations.LoginScreenDestination
import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationResult
import com.ayoprez.sobuu.ui.theme.DarkLava
import com.ayoprez.sobuu.ui.theme.SobuuTheme
import com.ayoprez.sobuu.ui.theme.SourceSans
import com.ayoprez.sobuu.ui.theme.SpanishGray
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun HomeScreen(
    nav: DestinationsNavigator,
    loginViewModel: LoginViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val searchState = searchViewModel.state
    var isSearchBarFocused by remember { mutableStateOf(false) }
    val activity = (LocalContext.current as? Activity)
    val focus = LocalFocusManager.current

    LaunchedEffect(loginViewModel, context) {
        loginViewModel.authResult.collect { result ->
            if (result == AuthenticationResult.LoggedOut<Unit>()) {
                nav.navigate(LoginScreenDestination) {
                    popUpTo(HomeScreenDestination.route) {
                        inclusive = true
                    }
                }
            }
        }
    }

    SobuuTheme(
        darkTheme = false,
    ) {
        BackPressHandler(onBackPressed = {
            if(!isSearchBarFocused) {
                activity?.finish()
            } else {
                if(searchState.searchTerm.isNotBlank()) {
                    searchViewModel.onEvent(
                        SearchUIEvent.cleanSearchTerm
                    )
                } else {
                    focus.clearFocus()
                }
            }
        })

        Scaffold(
            topBar = {
                TopAppBarWithSearchAndProfile(
                    nav = nav,
                    text = stringResource(id = R.string.app_name),
                    onSearchFieldValueChange = {
                        searchViewModel.onEvent(
                            SearchUIEvent.SearchTermChanged(it, Locale.current.language)
                        )
                    },
                    searchFieldValue = searchState.searchTerm,
                    onSearchButtonClick = {
                        searchViewModel.onEvent(
                            SearchUIEvent.searchTerm
                        )
                    },
                    clearTextButtonClick = {
                        searchViewModel.onEvent(
                            SearchUIEvent.cleanSearchTerm
                        )
                    },
                    onSearchFieldFocusChange = {
                        isSearchBarFocused = it
                    },
                )
            },
            content = {
                Content(
                    nav = nav,
                    isSearchBarFocus = isSearchBarFocused,
                    modifier = Modifier.padding(it),
                    homeViewModel = homeViewModel
                )
            }
        )
    }

}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    nav: DestinationsNavigator? = null,
    isSearchBarFocus: Boolean = false,
    homeViewModel: HomeViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
            .then(modifier),
    ) {
        if(isSearchBarFocus) {
            SearchListScreen(nav = nav)
        } else {
            HomeContent(nav = nav, homeViewModel = homeViewModel)
        }
    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    nav: DestinationsNavigator? = null,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    var currentSection: Int by remember { mutableStateOf(0) }

    Column {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .then(modifier),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            TextButton(onClick = {
                currentSection = 0
            }) {
                Text(
                    text = "Currently reading",
                    style = TextStyle(
                        color = if (currentSection == 0) DarkLava else SpanishGray,
                        fontSize = if (currentSection == 0) 20.sp else 14.sp,
                        fontFamily = SourceSans
                    )
                )
            }
            TextButton(onClick = {
                currentSection = 1
            }) {
                Text(
                    text = "Already read",
                    style = TextStyle(
                        color = if (currentSection == 1) DarkLava else SpanishGray,
                        fontSize = if (currentSection == 1) 20.sp else 14.sp,
                        fontFamily = SourceSans
                    )
                )
            }
            TextButton(onClick = {
                currentSection = 2
            }) {
                Text(
                    text = "Give up",
                    style = TextStyle(
                        color = if (currentSection == 2) DarkLava else SpanishGray,
                        fontSize = if (currentSection == 2) 20.sp else 14.sp,
                        fontFamily = SourceSans
                    )
                )
            }
        }

        when (currentSection) {
            0 -> SectionCurrentlyReading(homeViewModel = homeViewModel)
            1 -> SectionAlreadyRead(homeViewModel = homeViewModel)
            2 -> SectionGiveUp(homeViewModel = homeViewModel)
        }
    }
}

@Composable
fun SectionCurrentlyReading(homeViewModel: HomeViewModel) {

    val bookList = homeViewModel.currentlyReadingBooksList
    val listState = rememberLazyListState()

    LazyRow(
        state = listState,
        contentPadding = PaddingValues(5.dp),
    ) {
        items(bookList?.size ?: 0) { position ->
            val book = bookList?.get(position) ?: return@items

        }
    }
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(5.dp),
    ) {
        items(bookList?.size ?: 0) { position ->
            val book = bookList?.get(position) ?: return@items

        }
    }
}

@Composable
fun SectionAlreadyRead(homeViewModel: HomeViewModel) {}

@Composable
fun SectionGiveUp(homeViewModel: HomeViewModel) {}

@Composable
fun BackPressHandler(
    backPressedDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    onBackPressed: () -> Unit
) {
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    DisposableEffect(key1 = backPressedDispatcher) {
        backPressedDispatcher?.addCallback(backCallback)

        onDispose {
            backCallback.remove()
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
        onSearchFieldFocusChange = {},
    )
}

@Preview(showSystemUi = true, showBackground = true, group = "Done")
@Composable
fun ComposableHomeScreenContentPreview() {
    HomeContent()
}

