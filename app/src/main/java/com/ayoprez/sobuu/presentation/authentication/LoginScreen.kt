package com.ayoprez.sobuu.presentation.authentication

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoprez.sobuu.presentation.destinations.LoginScreenDestination
import com.ayoprez.sobuu.presentation.destinations.WelcomeScreenDestination
import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationResult
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@RootNavGraph(start = true)
@Destination
fun LoginScreen(
    nav: DestinationsNavigator,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current
    LaunchedEffect(viewModel, context) {
        viewModel.authResult.collect { result ->
            when(result) {
                is AuthenticationResult.Authorized -> {
                    nav.navigate(WelcomeScreenDestination) {
                        popUpTo(LoginScreenDestination.route) {
                            inclusive = true
                        }
                    }
                }
                is AuthenticationResult.Unauthorized -> {
                    Toast.makeText(context, "Not authorized", Toast.LENGTH_LONG).show()
                }
                is AuthenticationResult.Error -> {
                    viewModel.handleError(result.error)
                }
                is AuthenticationResult.LoggedOut -> {
                    nav.navigate(LoginScreenDestination) {
                        popUpTo(WelcomeScreenDestination.route) {
                            inclusive = true
                        }
                    }
                }
                is AuthenticationResult.Registered -> TODO()
                is AuthenticationResult.ResetPassword -> TODO()
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = state.loginUsername,
            onValueChange = {
                viewModel.onEvent(AuthenticationUIEvent.LoginUsernameChanged(it))
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Username")
            }
        )
        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            value = state.loginPassword,
            onValueChange = {
                viewModel.onEvent(AuthenticationUIEvent.LoginPasswordChanged(it))
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Password")
            }
        )
        Spacer(modifier = Modifier.height(25.dp))
        Button(
            onClick = {
                viewModel.onEvent(AuthenticationUIEvent.loginUser)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Login")
        }
    }

    if(state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}