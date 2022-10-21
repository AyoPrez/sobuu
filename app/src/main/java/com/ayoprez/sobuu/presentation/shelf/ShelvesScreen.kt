package com.ayoprez.sobuu.presentation.shelf

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Composable
@Destination
fun ShelvesScreen(
    nav: DestinationsNavigator,
    shelfViewModel: ShelfViewModel = hiltViewModel(),
) {

}