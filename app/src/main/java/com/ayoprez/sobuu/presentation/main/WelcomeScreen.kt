package com.ayoprez.sobuu.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoprez.sobuu.presentation.authentication.AuthenticationUIEvent
import com.ayoprez.sobuu.presentation.authentication.LoginViewModel
import com.ayoprez.sobuu.presentation.destinations.LoginScreenDestination
import com.ayoprez.sobuu.presentation.destinations.WelcomeScreenDestination
import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationResult
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun WelcomeScreen(
    nav: DestinationsNavigator,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(viewModel, context) {
        viewModel.authResult.collect { result ->
            if (result == AuthenticationResult.LoggedOut<Unit>()) {
                nav.navigate(LoginScreenDestination) {
                    popUpTo(WelcomeScreenDestination.route) {
                        inclusive = true
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Welcome")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            viewModel.onEvent(AuthenticationUIEvent.logoutUser)
        }) {
            Text("Logout")
        }
    }
}