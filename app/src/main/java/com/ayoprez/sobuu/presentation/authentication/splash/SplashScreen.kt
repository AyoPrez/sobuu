package com.ayoprez.sobuu.presentation.authentication.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.presentation.destinations.HomeScreenDestination
import com.ayoprez.sobuu.presentation.destinations.LoginScreenDestination
import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationResult
import com.ayoprez.sobuu.ui.theme.DarkLava
import com.ayoprez.sobuu.ui.theme.GreenSheen
import com.ayoprez.sobuu.ui.theme.Solway
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
                            popUpTo(LoginScreenDestination.route) {
                                inclusive = true
                            }
                        }
                    }
                    is AuthenticationResult.Unauthorized -> {
                        nav?.navigate(LoginScreenDestination)
                    }
                    is AuthenticationResult.Error -> {
                        viewModel.handleError(result.error)
                    }
                    is AuthenticationResult.LoggedOut -> TODO()
                    is AuthenticationResult.Registered -> TODO()
                    is AuthenticationResult.ResetPassword -> TODO()
                }
            }
        }

        SplashContent()
    }
}

@Composable
fun SplashContent() {
    Box(
        modifier = Modifier.background(GreenSheen),
        contentAlignment = Alignment.Center
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


@Preview(showSystemUi = true, showBackground = true, group = "Done")
@Composable
fun ComposableSplashContentPreview() {
    SplashContent()
}