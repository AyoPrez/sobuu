package com.ayoprez.sobuu.presentation.book

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.presentation.custom_widgets.SimpleTransparentTopAppBar
import com.ayoprez.sobuu.ui.theme.WhiteBlue
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun BookCoverScreen(
    nav: DestinationsNavigator? = null,
    cover: String,
) {
    Scaffold(
        topBar = {
            SimpleTransparentTopAppBar(
                nav = nav,
            )
        },
        content = {
            Box(
                modifier= Modifier
                    .background(WhiteBlue)
                    .fillMaxSize()
            ) {
                AsyncImage(
                    model = cover,
                    placeholder = painterResource(id = R.drawable.ic_cover_placeholder),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                )
            }
        }
    )
}
