package com.ayoprez.sobuu.presentation.shelf

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.presentation.custom_widgets.CustomBottomAppBar
import com.ayoprez.sobuu.presentation.custom_widgets.TopAppBarWithSearchAndProfile
import com.ayoprez.sobuu.presentation.main.BackPressHandler
import com.ayoprez.sobuu.presentation.main.SearchUIEvent
import com.ayoprez.sobuu.presentation.main.SearchViewModel
import com.ayoprez.sobuu.ui.theme.SobuuTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun ShelvesScreen(
    nav: DestinationsNavigator,
    shelfViewModel: ShelfViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val searchState = searchViewModel.state
    var isSearchBarFocused by remember { mutableStateOf(false) }
    val activity = (LocalContext.current as? Activity)
    val focus = LocalFocusManager.current

    SobuuTheme(
        darkTheme = false,
    ) {
        BackPressHandler(onBackPressed = {
            if (!isSearchBarFocused) {
                activity?.finish()
            } else {
                if (searchState.searchTerm.isNotBlank()) {
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
                Box(modifier = Modifier.padding(it))
            },
            bottomBar = { CustomBottomAppBar(nav = nav) }
        )
    }
}