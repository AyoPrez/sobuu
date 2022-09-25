package com.ayoprez.sobuu.presentation.authentication

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.presentation.destinations.LoginScreenDestination
import com.ayoprez.sobuu.presentation.destinations.WelcomeScreenDestination
import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationResult
import com.ayoprez.sobuu.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@RootNavGraph(start = true)
@Destination
fun LoginScreen(
    nav: DestinationsNavigator?,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current
    LaunchedEffect(viewModel, context) {
        viewModel.authResult.collect { result ->
            when (result) {
                is AuthenticationResult.Authorized -> {
                    nav?.navigate(WelcomeScreenDestination) {
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
                    nav?.navigate(LoginScreenDestination) {
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
            .background(GreenSheen),
        verticalArrangement = Arrangement.Bottom,
    ) {

        LoginAppTitle()

        LoginForm(
            nameFieldValue = state.loginUsername,
            onNameValueChange = {
                viewModel.onEvent(
                    AuthenticationUIEvent.LoginUsernameChanged(
                        it
                    )
                )
            },
            passwordFieldValue = state.loginPassword,
            onPasswordValueChange = {
                viewModel.onEvent(
                    AuthenticationUIEvent.LoginPasswordChanged(
                        it
                    )
                )
            },
            onLoginButtonClick = { viewModel.onEvent(AuthenticationUIEvent.loginUser) }
        )

        Spacer(modifier = Modifier.height(45.dp))

        OptionsButtons(
            onForgotPasswordButtonClick = { viewModel.onEvent(AuthenticationUIEvent.forgotPassword) },
            onCreateNewAccountButtonClick = { viewModel.onEvent(AuthenticationUIEvent.createNewAccount) },
        )

        LegalButtons(
            onTermsAndConditionsButtonClick = { viewModel.onEvent(AuthenticationUIEvent.openTermsAndConditions) },
            onPrivacyPolicyButtonClick = { viewModel.onEvent(AuthenticationUIEvent.openPrivacyPolicy) },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginForm(
    nameFieldValue: String,
    onNameValueChange: (String) -> Unit,
    passwordFieldValue: String,
    onPasswordValueChange: (String) -> Unit,
    onLoginButtonClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
            .background(GreenSheen),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = nameFieldValue,
            onValueChange = onNameValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(
                        topStart = 10.dp,
                        topEnd = 10.dp
                    ),
                    color = WhiteBlue
                ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = DarkLava,
                unfocusedBorderColor = DarkLava,
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.auth_username),
                    style = TextStyle(
                        color = SpanishGray,
                        fontFamily = SourceSans,
                        fontSize = 20.sp,
                    )
                )
            },
            shape = RoundedCornerShape(
                topStart = 10.dp,
                topEnd = 10.dp
            ),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "",
                    tint = SpanishGray,
                )
            }
        )

        OutlinedTextField(
            value = passwordFieldValue,
            onValueChange = onPasswordValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(
                        bottomStart = 10.dp,
                        bottomEnd = 10.dp,
                    ),
                    color = WhiteBlue
                ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = DarkLava,
                unfocusedBorderColor = DarkLava
            ),
            placeholder = {
                Text(
                    stringResource(id = R.string.auth_password),
                    style = TextStyle(
                        color = SpanishGray,
                        fontFamily = SourceSans,
                        fontSize = 20.sp,
                    )
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
            keyboardActions = KeyboardActions(
                onSend = {
                    onLoginButtonClick()
                }
            ),
            shape = RoundedCornerShape(
                bottomStart = 10.dp,
                bottomEnd = 10.dp,
            ),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "",
                    tint = SpanishGray
                )
            }
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
        TextButton(
            onClick = onForgotPasswordButtonClick,
        ) {
            Text(
                stringResource(id = R.string.auth_forgot_password),
                style = TextStyle(
                    fontSize = 16.sp,
                    color = DarkLava,
                    fontWeight = FontWeight.Normal,
                    fontFamily = SourceSans
                ),
                textDecoration = TextDecoration.Underline,
                textAlign = TextAlign.Center,
            )

        }
        Spacer(modifier = Modifier.height(25.dp))
        FilledTonalButton(
            onClick = onCreateNewAccountButtonClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkLava,
                contentColor = WhiteBlue,
                disabledContainerColor = SpanishGray,
                disabledContentColor = WhiteBlue,
            )
        ) {
            Text(
                stringResource(id = R.string.auth_create_account),
                style = TextStyle(
                    fontSize = 22.sp,
                    color = WhiteBlue,
                    fontWeight = FontWeight.Normal,
                    fontFamily = SourceSans
                ),
                textAlign = TextAlign.Center,
            )
        }
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
        TextButton(
            onClick = onTermsAndConditionsButtonClick
        ) {
            Text(
                stringResource(id = R.string.terms),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = SourceSans,
                    color = WhiteBlue
                ),
            )
        }

        TextButton(
            onClick = onPrivacyPolicyButtonClick
        ) {
            Text(
                stringResource(id = R.string.privacy),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = SourceSans,
                    color = WhiteBlue
                ),
            )
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
        onLoginButtonClick = {}
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