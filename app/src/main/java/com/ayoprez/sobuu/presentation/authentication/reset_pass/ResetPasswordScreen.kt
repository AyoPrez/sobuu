package com.ayoprez.sobuu.presentation.authentication.reset_pass

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.presentation.authentication.EmailType
import com.ayoprez.sobuu.presentation.custom_widgets.CompleteRoundedOutlineTextField
import com.ayoprez.sobuu.presentation.custom_widgets.CustomTopAppBar
import com.ayoprez.sobuu.presentation.custom_widgets.RoundedFillButton
import com.ayoprez.sobuu.presentation.destinations.SentEmailScreenDestination
import com.ayoprez.sobuu.ui.theme.GreenSheen
import com.ayoprez.sobuu.ui.theme.WhiteBlue
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun ResetPasswordScreen(
    nav: DestinationsNavigator?,
) {
    val viewModel: ResetPassViewModel = hiltViewModel()
    val state = viewModel.state

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
            ResetPasswordForm(
                emailFieldValue = state.forgotEmail,
                onEmailValueChange = {
                    viewModel.onEvent(ResetPassUIEvent.ForgotPasswordEmailChanged(it))
                },
                onResetPasswordButtonClick = {
                    //viewModel.onEvent(ResetPassUIEvent.resetPassword)
                    nav?.navigate(SentEmailScreenDestination(emailType = EmailType.RESET_PASSWORD))
                }
            )
        },
    )
}

@Composable
private fun ResetPasswordForm(
    emailFieldValue: String,
    onEmailValueChange: (String) -> Unit,
    onResetPasswordButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
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
        )

        RoundedFillButton(
            onClick = { onResetPasswordButtonClick() },
            text = stringResource(id = R.string.auth_reset_password),
        )
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
