package com.ayoprez.sobuu.presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.presentation.authentication.login.LoginViewModel
import com.ayoprez.sobuu.presentation.destinations.LoginScreenDestination
import com.ayoprez.sobuu.presentation.destinations.ProfileScreenDestination
import com.ayoprez.sobuu.presentation.destinations.WelcomeScreenDestination
import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationResult
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun WelcomeScreen(
    nav: DestinationsNavigator,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(loginViewModel, context) {
        loginViewModel.authResult.collect { result ->
            if (result == AuthenticationResult.LoggedOut<Unit>()) {
                nav.navigate(LoginScreenDestination) {
                    popUpTo(WelcomeScreenDestination.route) {
                        inclusive = true
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(text = "Welcome")
        Spacer(modifier = Modifier.height(16.dp))
        FilledIconButton(onClick = {
            nav.navigate(ProfileScreenDestination())
        }) {
            Text(text = stringResource(id = R.string.profile_title))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            //loginViewModel.onEvent(AuthenticationUIEvent.logoutUser)
        }) {
            Text(stringResource(id = R.string.auth_logout))
        }
    }
}