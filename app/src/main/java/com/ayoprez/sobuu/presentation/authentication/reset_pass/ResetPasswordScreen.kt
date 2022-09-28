package com.ayoprez.sobuu.presentation.authentication.reset_pass

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.presentation.authentication.EmailType
import com.ayoprez.sobuu.presentation.custom_widgets.CompleteRoundedOutlineTextField
import com.ayoprez.sobuu.presentation.custom_widgets.CustomTopAppBar
import com.ayoprez.sobuu.presentation.custom_widgets.RoundedFillButton
import com.ayoprez.sobuu.presentation.destinations.SentEmailScreenDestination
import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationError
import com.ayoprez.sobuu.ui.theme.GreenSheen
import com.ayoprez.sobuu.ui.theme.SourceSans
import com.ayoprez.sobuu.ui.theme.Vermilion
import com.ayoprez.sobuu.ui.theme.WhiteBlue
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
@Destination
fun ResetPasswordScreen(
    nav: DestinationsNavigator?,
) {
    val viewModel: ResetPassViewModel = hiltViewModel()
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(viewModel, context) {
        viewModel.authResult.collect { result ->
            if (result.error != null) {
                viewModel.handleError(result.error)
            } else {
                nav?.navigate(SentEmailScreenDestination(emailType = EmailType.RESET_PASSWORD))
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomTopAppBar(
                nav = nav,
                text = stringResource(id = R.string.auth_reset_password),
                backgroundColor = GreenSheen,
                iconColor = WhiteBlue,
                titleColor = WhiteBlue,
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GreenSheen),
            ) {
                ResetPasswordForm(
                    emailFieldValue = state.forgotEmail,
                    onEmailValueChange = {
                        viewModel.onEvent(ResetPassUIEvent.ForgotPasswordEmailChanged(it))
                    },
                    onResetPasswordButtonClick = {
                        viewModel.onEvent(ResetPassUIEvent.resetPassword)
                    },
                    isError = state.error != null,
                )

                if (state.error != null) {
                    Spacer(modifier = Modifier.height(50.dp))

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
                }
            }
        },
    )
}

@Composable
private fun ResetPasswordForm(
    emailFieldValue: String,
    onEmailValueChange: (String) -> Unit,
    onResetPasswordButtonClick: () -> Unit,
    isError: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(GreenSheen),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(150.dp))
        CompleteRoundedOutlineTextField(
            fieldValue = emailFieldValue,
            onFieldValueChange = onEmailValueChange,
            placeholderText = stringResource(id = R.string.auth_email),
            icon = Icons.Filled.Email,
            onKeyboardActionClicked = onResetPasswordButtonClick,
            isError = isError,
        )

        RoundedFillButton(
            onClick = { onResetPasswordButtonClick() },
            text = stringResource(id = R.string.auth_reset_password),
        )
    }
}

@Composable
fun getStringFromError(error: AuthenticationError?): String {
    return when (error) {
        is AuthenticationError.EmptyCredentialsError -> {
            stringResource(id = R.string.error_empty_credentials)
        }
        is AuthenticationError.TimeOutError -> {
            stringResource(id = R.string.error_timeout)
        }
        is AuthenticationError.WrongEmailFormatError -> {
            stringResource(id = R.string.error_wrong_email_format)
        }
        else -> {
            stringResource(id = R.string.error_unknown)
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, group = "Done")
@Composable
fun ComposableResetPasswordPreview() {
    ResetPasswordForm(
        emailFieldValue = "",
        onEmailValueChange = {},
        onResetPasswordButtonClick = {}
    )
}
