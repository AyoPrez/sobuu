package com.ayoprez.sobuu.presentation.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.presentation.destinations.LoginScreenDestination
import com.ayoprez.sobuu.ui.theme.GreenSheen
import com.ayoprez.sobuu.ui.theme.SourceSans
import com.ayoprez.sobuu.ui.theme.WhiteBlue
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

enum class EmailType {
    RESET_PASSWORD, VERIFICATION
}

@Composable
@Destination
fun SentEmailScreen(
    nav: DestinationsNavigator?,
    emailType: EmailType,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = GreenSheen),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .align(Alignment.End),
            onClick = {
            nav?.navigate(LoginScreenDestination) {
                popUpTo(LoginScreenDestination.route) {
                    inclusive = true
                }
            }
        }) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "",
                tint = WhiteBlue
            )
        }

        EmailAnimation()

        Text(
            text = if(emailType == EmailType.VERIFICATION)
                stringResource(id = R.string.auth_verification_email)
            else
                stringResource(id = R.string.auth_reset_password_email),
            style = TextStyle(
                color = WhiteBlue,
                fontFamily = SourceSans,
                fontSize = 20.sp,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        )
    }
}

@Composable
fun EmailAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.email_tailwind))
    val progress by animateLottieCompositionAsState(composition)

    LottieAnimation(
        modifier = Modifier
            .width(120.dp)
            .height(120.dp),
        composition = composition,
        progress = { progress },
    )
}

@Preview(showSystemUi = true, showBackground = true, group = "Done")
@Composable
fun ComposableResetPasswordPreview() {
    SentEmailScreen(
        null,
        EmailType.VERIFICATION
    )
}
