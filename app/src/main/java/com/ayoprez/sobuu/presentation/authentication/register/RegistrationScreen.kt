package com.ayoprez.sobuu.presentation.authentication.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.presentation.authentication.EmailType
import com.ayoprez.sobuu.presentation.authentication.TextType
import com.ayoprez.sobuu.presentation.custom_widgets.*
import com.ayoprez.sobuu.presentation.destinations.LongTextScreenDestination
import com.ayoprez.sobuu.presentation.destinations.SentEmailScreenDestination
import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationError
import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationResult
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
    val context = LocalContext.current

    LaunchedEffect(viewModel, context) {
        viewModel.registrationResult.collect { result ->
            if (result is AuthenticationResult.Registered) {
                nav?.navigate(SentEmailScreenDestination(emailType = EmailType.VERIFICATION))
            } else {
                viewModel.handleError(result.error)
            }
        }
    }

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .background(GreenSheen),

                ) {
                if (state.error != null) {
                    Spacer(modifier = Modifier.height(60.dp))

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
                    Spacer(modifier = Modifier.height(80.dp))
                }

                RegistrationForm(
                    usernameFieldValue = state.registrationUsername,
                    onUsernameValueChange = { username ->
                        viewModel.onEvent(RegistrationUIEvent.RegistrationUsernameChanged(username))
                    },
                    emailFieldValue = state.registrationEmail,
                    onEmailValueChange = { email ->
                        viewModel.onEvent(RegistrationUIEvent.RegistrationEmailChanged(email))
                    },
                    passwordFieldValue = state.registrationPassword,
                    onPasswordValueChange = { password ->
                        viewModel.onEvent(RegistrationUIEvent.RegistrationPasswordChanged(password))
                    },
                    passwordConfirmationFieldValue = state.registrationPasswordConfirmation,
                    onPasswordConfirmationValueChange = { password ->
                        viewModel.onEvent(
                            RegistrationUIEvent.RegistrationPasswordConfirmationChanged(
                                password
                            )
                        )
                    },
                    firstNameFieldValue = state.registrationFirstname,
                    onFirstNameValueChange = { firstName ->
                        viewModel.onEvent(RegistrationUIEvent.RegistrationFirstNameChanged(firstName))
                    },
                    lastNameFieldValue = state.registrationLastname,
                    onLastNameValueChange = { lastName ->
                        viewModel.onEvent(RegistrationUIEvent.RegistrationLastNameChanged(lastName))
                    },
                    privacyPolicyConfirmationSwitchValue = state.privacyPolicyConfirmationSwitch,
                    onPrivacyPolicyConfirmationSwitchValueChange = { policy ->
                        viewModel.onEvent(
                            RegistrationUIEvent.PrivacyPolicyConfirmationSwitchChanged(
                                policy
                            )
                        )
                    },
                    onCreateAccountButtonClick = {
                        viewModel.onEvent(RegistrationUIEvent.registerUser)
                    },
                    usernameError = state.error != null,
                    emailError = state.error != null,
                    passwordError = state.error != null,
                    confirmationPasswordError = state.error != null,
                    firstNameError = state.error != null,
                    lastNameError = state.error != null,
                )
            }
        },
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun RegistrationForm(
    nav: DestinationsNavigator? = null,
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
    usernameError: Boolean = false,
    emailError: Boolean = false,
    passwordError: Boolean = false,
    confirmationPasswordError: Boolean = false,
    firstNameError: Boolean = false,
    lastNameError: Boolean = false,
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
        TopRoundedOutlinedTextField(
            fieldValue = usernameFieldValue,
            onFieldValueChange = onUsernameValueChange,
            placeholderText = stringResource(id = R.string.auth_username),
            icon = Icons.Filled.Person,
            isError = usernameError,
        )

        NotRoundedOutlineTextField(
            fieldValue = emailFieldValue,
            onFieldValueChange = onEmailValueChange,
            placeholderText = stringResource(id = R.string.auth_email),
            icon = Icons.Filled.Email,
            isError = emailError,
        )

        NotRoundedOutlineTextField(
            fieldValue = passwordFieldValue,
            onFieldValueChange = onPasswordValueChange,
            placeholderText = stringResource(id = R.string.auth_password),
            icon = Icons.Filled.Lock,
            passwordField = true,
            isError = passwordError,
        )

        BottomRoundedOutlinedTextField(
            fieldValue = passwordConfirmationFieldValue,
            onFieldValueChange = onPasswordConfirmationValueChange,
            placeholderText = stringResource(id = R.string.auth_confirm_password),
            icon = Icons.Filled.Lock,
            onKeyboardActionClicked = { focusManager.moveFocus(FocusDirection.Down) },
            passwordField = true,
            isError = confirmationPasswordError,
        )

        Spacer(modifier = Modifier.height(25.dp))

        TopRoundedOutlinedTextField(
            fieldValue = firstNameFieldValue,
            onFieldValueChange = onFirstNameValueChange,
            placeholderText = stringResource(id = R.string.auth_first_name_account),
            icon = Icons.Filled.Person,
            isError = firstNameError,
        )

        BottomRoundedOutlinedTextField(
            fieldValue = lastNameFieldValue,
            onFieldValueChange = onLastNameValueChange,
            placeholderText = stringResource(id = R.string.auth_last_name_account),
            icon = Icons.Filled.Person,
            onKeyboardActionClicked = { keyboardController?.hide() },
            isError = lastNameError,
        )

        Spacer(modifier = Modifier.height(25.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp),
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
            AcceptanceLineWithLinks(nav = nav)
        }

        Spacer(modifier = Modifier.height(25.dp))

        RoundedFillButton(
            onClick = onCreateAccountButtonClick,
            text = stringResource(id = R.string.auth_create_account),
        )
    }
}

@Composable
fun AcceptanceLineWithLinks(
    nav: DestinationsNavigator?
) {
    val annotatedString = buildAnnotatedString {
        append("${stringResource(id = R.string.auth_privacy_confirmation_1)} ")

        pushStringAnnotation(
            tag = "terms",
            annotation = "terms and conditions"
        )
        withStyle(style = SpanStyle(color = Vermilion)) {
            append(stringResource(id = R.string.terms))
        }
        pop()

        append(" ${stringResource(id = R.string.auth_privacy_confirmation_2)} ")

        pushStringAnnotation(
            tag = "privacy",
            annotation = "privacy policy"
        )

        withStyle(style = SpanStyle(color = Vermilion)) {
            append(stringResource(id = R.string.privacy))
        }

        pop()

        append(" ${stringResource(id = R.string.auth_privacy_confirmation_3)}")
    }

    ClickableText(
        text = annotatedString,
        modifier = Modifier.padding(top = 10.dp),
        style = TextStyle(color = WhiteBlue),
        onClick = { offset ->

            annotatedString.getStringAnnotations(
                tag = "terms",
                start = offset,
                end = offset
            ).firstOrNull()?.let {
                nav?.navigate(LongTextScreenDestination(textType = TextType.TERMS_AND_CONDITIONS))
            }

            annotatedString.getStringAnnotations(
                tag = "privacy",
                start = offset,
                end = offset
            ).firstOrNull()?.let {
                nav?.navigate(LongTextScreenDestination(textType = TextType.PRIVACY_POLICY))
            }
        })
}

@Composable
fun getStringFromError(error: AuthenticationError?): String {
    return when (error) {
        is AuthenticationError.InvalidCredentials -> {
            stringResource(id = R.string.error_invalid_registration_fields)
        }
        is AuthenticationError.EmptyCredentialsError -> {
            stringResource(id = R.string.error_empty_registration_fields)
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
fun ComposableTextLineWithLinksPreview() {
    AcceptanceLineWithLinks(null)
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