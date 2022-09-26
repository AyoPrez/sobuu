package com.ayoprez.sobuu.presentation.authentication.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.presentation.custom_widgets.*
import com.ayoprez.sobuu.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun RegistrationScreen(
    nav: DestinationsNavigator?,
) {
    val viewModel: RegistrationViewModel = hiltViewModel()
    val state = viewModel.state

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomTopAppBar(
                nav = nav,
                text = stringResource(id = R.string.auth_create_account),
                backgroundColor = GreenSheen,
                titleColor = WhiteBlue,
                iconColor = WhiteBlue,
            )
        },
        content = {
            Box(modifier = Modifier.fillMaxSize().background(GreenSheen)) {
                RegistrationForm(
                    usernameFieldValue = state.registrationUsername,
                    onUsernameValueChange = {
                        viewModel.onEvent(RegistrationUIEvent.RegistrationUsernameChanged(it))
                    },
                    emailFieldValue = state.registrationEmail,
                    onEmailValueChange = {
                        viewModel.onEvent(RegistrationUIEvent.RegistrationEmailChanged(it))
                    },
                    passwordFieldValue = state.registrationPassword,
                    onPasswordValueChange = {
                        viewModel.onEvent(RegistrationUIEvent.RegistrationPasswordChanged(it))
                    },
                    passwordConfirmationFieldValue = state.registrationPasswordConfirmation,
                    onPasswordConfirmationValueChange = {
                        viewModel.onEvent(
                            RegistrationUIEvent.RegistrationPasswordConfirmationChanged(
                                it
                            )
                        )
                    },
                    firstNameFieldValue = state.registrationFirstname,
                    onFirstNameValueChange = {
                        viewModel.onEvent(RegistrationUIEvent.RegistrationFirstNameChanged(it))
                    },
                    lastNameFieldValue = state.registrationLastname,
                    onLastNameValueChange = {
                        viewModel.onEvent(RegistrationUIEvent.RegistrationLastNameChanged(it))
                    },
                    privacyPolicyConfirmationSwitchValue = state.privacyPolicyConfirmationSwitch,
                    onPrivacyPolicyConfirmationSwitchValueChange = {
                        viewModel.onEvent(
                            RegistrationUIEvent.PrivacyPolicyConfirmationSwitchChanged(
                                it
                            )
                        )
                    },
                    onCreateAccountButtonClick = {
                        viewModel.onEvent(RegistrationUIEvent.registerUser)
                    }
                )
            }
        },
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun RegistrationForm(
    usernameFieldValue: String,
    onUsernameValueChange: (String) -> Unit,
    emailFieldValue: String,
    onEmailValueChange: (String) -> Unit,
    passwordFieldValue: String,
    onPasswordValueChange: (String) -> Unit,
    passwordConfirmationFieldValue: String,
    onPasswordConfirmationValueChange: (String) -> Unit,
    firstNameFieldValue: String,
    onFirstNameValueChange: (String) -> Unit,
    lastNameFieldValue: String,
    onLastNameValueChange: (String) -> Unit,
    privacyPolicyConfirmationSwitchValue: Boolean,
    onPrivacyPolicyConfirmationSwitchValueChange: (Boolean) -> Unit,
    onCreateAccountButtonClick: () -> Unit,
) {

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .background(GreenSheen),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        TopRoundedOutlinedTextField(
            fieldValue = usernameFieldValue,
            onFieldValueChange = onUsernameValueChange,
            placeholderText = stringResource(id = R.string.auth_username),
            icon = Icons.Filled.Person,
        )

        NotRoundedOutlineTextField(
            fieldValue = emailFieldValue,
            onFieldValueChange = onEmailValueChange,
            placeholderText = stringResource(id = R.string.auth_email),
            icon = Icons.Filled.Email,
        )

        NotRoundedOutlineTextField(
            fieldValue = passwordFieldValue,
            onFieldValueChange = onPasswordValueChange,
            placeholderText = stringResource(id = R.string.auth_password),
            icon = Icons.Filled.Lock,
        )

        BottomRoundedOutlinedTextField(
            fieldValue = passwordConfirmationFieldValue,
            onFieldValueChange = onPasswordConfirmationValueChange,
            placeholderText = stringResource(id = R.string.auth_confirm_password),
            icon = Icons.Filled.Lock,
            onKeyboardActionClicked = {focusManager.moveFocus(FocusDirection.Down)}
        )

        Spacer(modifier = Modifier.height(25.dp))

        TopRoundedOutlinedTextField(
            fieldValue = firstNameFieldValue,
            onFieldValueChange = onFirstNameValueChange,
            placeholderText = stringResource(id = R.string.auth_first_name_account),
            icon = Icons.Filled.Person,
        )

        BottomRoundedOutlinedTextField(
            fieldValue = lastNameFieldValue,
            onFieldValueChange = onLastNameValueChange,
            placeholderText = stringResource(id = R.string.auth_last_name_account),
            icon = Icons.Filled.Person,
            onKeyboardActionClicked = {keyboardController?.hide()}
        )

        Spacer(modifier = Modifier.height(25.dp))

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Switch(
                colors = SwitchDefaults.colors(
                    uncheckedTrackColor = DarkLava,
                    uncheckedBorderColor = DarkLava,
                    uncheckedIconColor = SpanishGray,
                    uncheckedThumbColor = SpanishGray,
                    checkedTrackColor = Vermilion,
                    checkedBorderColor = Vermilion,
                    checkedIconColor = WhiteBlue,
                    checkedThumbColor = WhiteBlue,
                ),
                checked = privacyPolicyConfirmationSwitchValue,
                onCheckedChange = onPrivacyPolicyConfirmationSwitchValueChange
            )
            Spacer(Modifier.width(18.dp))
            Text(
                text = stringResource(id = R.string.auth_privacy_confirmation),
                style = TextStyle(
                    color = WhiteBlue,
                    fontFamily = SourceSans,
                    fontSize = 16.sp,
                ),
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        RoundedFillButton(
            onClick = onCreateAccountButtonClick,
            text = stringResource(id = R.string.auth_create_account),
        )
    }
}


@Preview(showSystemUi = true, showBackground = true, group = "Done")
@Composable
fun ComposableRegistrationFormPreview() {
    RegistrationForm(
        usernameFieldValue = "",
        onUsernameValueChange = {},
        emailFieldValue = "",
        onEmailValueChange = {},
        passwordFieldValue = "",
        onPasswordValueChange = {},
        passwordConfirmationFieldValue = "",
        onPasswordConfirmationValueChange = {},
        firstNameFieldValue = "",
        onFirstNameValueChange = {},
        lastNameFieldValue = "",
        onLastNameValueChange = {},
        privacyPolicyConfirmationSwitchValue = true,
        onPrivacyPolicyConfirmationSwitchValueChange = {},
        onCreateAccountButtonClick = {},
    )
}