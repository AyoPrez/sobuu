package com.ayoprez.sobuu.presentation.book

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ayoprez.sobuu.presentation.custom_widgets.CustomTopAppBar
import com.ayoprez.sobuu.shared.models.Book
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun BookScreen(
    nav: DestinationsNavigator? = null,
    book: Book
) {

    Scaffold(
        topBar = {
            CustomTopAppBar(
                nav = nav,
                text = book.title,
            )
        },
        content = {
            BookScreenContent()
        }
    )
}

@Composable
fun BookScreenContent() {

}

@Preview
@Composable
fun BookScreenContentPreview() {
    BookScreenContent()
}