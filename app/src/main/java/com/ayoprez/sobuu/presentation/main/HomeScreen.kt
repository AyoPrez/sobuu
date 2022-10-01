package com.ayoprez.sobuu.presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.presentation.authentication.login.LoginViewModel
import com.ayoprez.sobuu.presentation.custom_widgets.TopAppBarWithSearchAndProfile
import com.ayoprez.sobuu.presentation.destinations.HomeScreenDestination
import com.ayoprez.sobuu.presentation.destinations.LoginScreenDestination
import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationResult
import com.ayoprez.sobuu.ui.theme.SobuuTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun HomeScreen(
    nav: DestinationsNavigator,
    loginViewModel: LoginViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val searchState = searchViewModel.state
    var isSearchBarFocused by remember { mutableStateOf(false) }

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
        Scaffold(
            topBar = {
                TopAppBarWithSearchAndProfile(
                    nav = nav,
                    text = stringResource(id = R.string.app_name),
                    onSearchFieldValueChange = {
                        searchViewModel.onEvent(
                            SearchUIEvent.SearchTermChanged(it)
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
                    isSearchBarFocus = isSearchBarFocused,
                )
            }
        )
    }

}

@Composable
fun Content(
    nav: DestinationsNavigator? = null,
    isSearchBarFocus: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 150.dp),
    ) {
        if(isSearchBarFocus) {
            SearchListScreen()
        } else {
            Text(text = "Welcome")
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
    Content()
}

