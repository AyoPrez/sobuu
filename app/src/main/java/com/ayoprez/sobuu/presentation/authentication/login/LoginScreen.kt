package com.ayoprez.sobuu.presentation.authentication.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.presentation.authentication.TextType
import com.ayoprez.sobuu.presentation.custom_widgets.*
import com.ayoprez.sobuu.presentation.destinations.*
import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationError
import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationResult
import com.ayoprez.sobuu.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Destination
fun LoginScreen(
    nav: DestinationsNavigator?,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(viewModel, context) {
        viewModel.authResult.collect { result ->
            if (result is AuthenticationResult.Authorized) {
                nav?.navigate(HomeScreenDestination) {
                    popUpTo(LoginScreenDestination.route) {
                        inclusive = true
                    }
                }
            } else {
                viewModel.handleError(result.error)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GreenSheen),
        verticalArrangement = Arrangement.Bottom,
    ) {

        LoginAppTitle()

        LoginForm(
            nameFieldValue = state.loginUsername,
            onNameValueChange = {
                viewModel.onEvent(
                    LoginUIEvent.LoginUsernameChanged(
                        it
                    )
                )
            },
            passwordFieldValue = state.loginPassword,
            onPasswordValueChange = {
                viewModel.onEvent(
                    LoginUIEvent.LoginPasswordChanged(
                        it
                    )
                )
            },
            onLoginButtonClick = {
                viewModel.onEvent(LoginUIEvent.loginUser)
                keyboardController?.hide()
            },
            nameIsError = state.error != null,
            passwordIsError = state.error != null,
        )

        if (state.error != null) {
            Spacer(modifier = Modifier.height(5.dp))

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

        OptionsButtons(
            onForgotPasswordButtonClick = {
                nav?.navigate(ResetPasswordScreenDestination)
            },
            onCreateNewAccountButtonClick = {
                nav?.navigate(RegistrationScreenDestination)
            },
        )

        LegalButtons(
            onTermsAndConditionsButtonClick = {
                nav?.navigate(LongTextScreenDestination(textType = TextType.TERMS_AND_CONDITIONS))
            },
            onPrivacyPolicyButtonClick = {
                nav?.navigate(LongTextScreenDestination(textType = TextType.PRIVACY_POLICY))
            },
        )
    }

    if (state.isLoading) {
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

@Composable
fun LoginAppTitle() {
    Text(
        stringResource(id = R.string.app_name),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 24.dp,
                top = 48.dp,
                end = 24.dp,
                bottom = 32.dp
            )
            .background(GreenSheen),
        style = TextStyle(
            fontSize = 110.sp,
            fontFamily = Solway,
            color = DarkLava,
        ),
        textAlign = TextAlign.Center,
    )
}

@Composable
fun LoginForm(
    nameFieldValue: String,
    onNameValueChange: (String) -> Unit,
    passwordFieldValue: String,
    onPasswordValueChange: (String) -> Unit,
    onLoginButtonClick: () -> Unit,
    nameIsError: Boolean,
    passwordIsError: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
            .background(GreenSheen),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopRoundedOutlinedTextField(
            fieldValue = nameFieldValue,
            onFieldValueChange = onNameValueChange,
            placeholderText = stringResource(id = R.string.auth_username),
            icon = Icons.Filled.Person,
            isError = nameIsError,
        )

        BottomRoundedOutlinedTextField(
            fieldValue = passwordFieldValue,
            onFieldValueChange = onPasswordValueChange,
            placeholderText = stringResource(id = R.string.auth_password),
            icon = Icons.Filled.Lock,
            onKeyboardActionClicked = onLoginButtonClick,
            passwordField = true,
            isError = passwordIsError,
        )

        Spacer(modifier = Modifier.height(25.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp),
        ) {
            Text(
                stringResource(id = R.string.auth_login),
                style = TextStyle(
                    fontSize = 32.sp,
                    color = WhiteBlue,
                    fontWeight = FontWeight.Bold,
                    fontFamily = SourceSans
                )
            )
            FilledIconButton(
                modifier = Modifier.size(62.dp),
                onClick = onLoginButtonClick,
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(
                    contentColor = WhiteBlue,
                    containerColor = Vermilion,
                    disabledContainerColor = SpanishGray,
                    disabledContentColor = WhiteBlue,
                )
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "",
                )
            }
        }
    }
}

@Composable
fun OptionsButtons(
    onForgotPasswordButtonClick: () -> Unit,
    onCreateNewAccountButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
            .background(GreenSheen),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CustomUnderlineTextButton(
            onClick = onForgotPasswordButtonClick,
            color = DarkLava,
            text = stringResource(id = R.string.auth_forgot_password),
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(25.dp))
        RoundedFillButton(
            onClick = onCreateNewAccountButtonClick,
            color = DarkLava,
            text = stringResource(id = R.string.auth_create_new_account),
        )
    }
}

@Composable
fun LegalButtons(
    onTermsAndConditionsButtonClick: () -> Unit,
    onPrivacyPolicyButtonClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
            .background(GreenSheen),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom,
    ) {
        CustomTextButton(
            onClick = onTermsAndConditionsButtonClick,
            text = stringResource(id = R.string.terms),
        )

        CustomTextButton(
            onClick = onPrivacyPolicyButtonClick,
            text = stringResource(id = R.string.privacy),
        )
    }
}

@Composable
fun getStringFromError(error: AuthenticationError?): String {
    return when (error) {
        is AuthenticationError.InvalidCredentials -> {
            stringResource(id = R.string.error_invalid_credentials)
        }
        is AuthenticationError.EmptyCredentialsError -> {
            stringResource(id = R.string.error_empty_credentials)
        }
        is AuthenticationError.InvalidSessionToken -> {
            stringResource(id = R.string.error_invalid_session_token)
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
fun ComposableLoginTitlePreview() {
    LoginAppTitle()
}

@Preview(showSystemUi = true, showBackground = true, group = "Done")
@Composable
fun ComposableLoginFormPreview() {
    LoginForm(
        nameFieldValue = "",
        onNameValueChange = {},
        passwordFieldValue = "",
        onPasswordValueChange = {},
        onLoginButtonClick = {},
        nameIsError = false,
        passwordIsError = false,
    )
}

@Preview(showSystemUi = true, showBackground = true, group = "Done")
@Composable
fun ComposableOtherOptionsButtonsPreview() {
    OptionsButtons(
        onCreateNewAccountButtonClick = {},
        onForgotPasswordButtonClick = {},
    )
}

@Preview(showSystemUi = true, showBackground = true, group = "Done")
@Composable
fun ComposableLegalButtonsPreview() {
    LegalButtons(
        onPrivacyPolicyButtonClick = {},
        onTermsAndConditionsButtonClick = {},
    )
}