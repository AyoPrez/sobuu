package com.ayoprez.sobuu.presentation.authentication.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.presentation.destinations.HomeScreenDestination
import com.ayoprez.sobuu.presentation.destinations.LoginScreenDestination
import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationError
import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationResult
import com.ayoprez.sobuu.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@RootNavGraph(start = true)
@Destination
fun SplashScreen(
    nav: DestinationsNavigator?,
    viewModel: SplashViewModel = hiltViewModel()
) {

    //TODO Add no internet connection detector logic and error message
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GreenSheen),
        contentAlignment = Alignment.Center,
    ) {

        val state = viewModel.state
        val context = LocalContext.current

        LaunchedEffect(viewModel, context) {
            viewModel.authResult.collect { result ->
                when (result) {
                    is AuthenticationResult.Authorized -> {
                        nav?.navigate(HomeScreenDestination) {
                            popUpTo(HomeScreenDestination.route) {
                                inclusive = true
                            }
                        }
                    }
                    is AuthenticationResult.Unauthorized -> {
                        nav?.navigate(LoginScreenDestination) {
                            popUpTo(LoginScreenDestination.route) {
                                inclusive = true
                            }
                        }
                    }
                    else -> {
                        viewModel.handleError(result.error)
                    }
                }
            }
        }

        Column(
            modifier = Modifier.background(GreenSheen),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SplashContent()

            if (state.error != null) {
                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    getStringFromError(error = state.error),
                    modifier = Modifier
                        .padding(start = 32.dp, end = 32.dp),
                    style = TextStyle(
                        color = Vermilion,
                        fontFamily = SourceSans,
                        fontSize = 20.sp
                    ),
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(5.dp))
            } else {
                Spacer(modifier = Modifier.height(45.dp))
            }
        }
    }
}

@Composable
fun SplashContent() {
    Box(
        modifier = Modifier.background(GreenSheen),
        contentAlignment = Alignment.Center,
    ) {
        Pulsating(
            pulseFraction = 1.2f
        ) {
            Text(
                stringResource(id = R.string.app_name),
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(GreenSheen),
                style = TextStyle(
                    fontSize = 80.sp,
                    fontFamily = Solway,
                    color = DarkLava,
                ),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun Pulsating(pulseFraction: Float = 1.2f, content: @Composable () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition()

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = pulseFraction,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(modifier = Modifier.scale(scale)) {
        content()
    }
}

@Composable
fun getStringFromError(error: AuthenticationError?): String {
    return when (error) {
        is AuthenticationError.TimeOutError -> {
            stringResource(id = R.string.error_timeout)
        }
        else -> {
            stringResource(id = R.string.error_unknown)
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, group = "Done")
@Composable
fun ComposableSplashContentPreview() {
    SplashContent()
}