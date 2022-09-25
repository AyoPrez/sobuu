package com.ayoprez.sobuu.presentation.authentication.reset_pass

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.ui.theme.*
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
            TopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.auth_reset_password),
                        style = TextStyle(
                            color = WhiteBlue,
                            fontSize = 24.sp,
                            fontFamily = SourceSans
                        )
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(GreenSheen),
                modifier = Modifier.fillMaxWidth(),
                navigationIcon = {
                    IconButton(onClick = {
                        nav?.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "",
                            tint = WhiteBlue,
                        )
                    }
                })
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GreenSheen),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedTextField(
                    value = state.forgotEmail,
                    onValueChange = {
                        viewModel.onEvent(ResetPassUIEvent.ForgotPasswordEmailChanged(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                        .background(
                            shape = RoundedCornerShape(10.dp),
                            color = WhiteBlue
                        ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = DarkLava,
                        unfocusedBorderColor = DarkLava,
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            viewModel.onEvent(ResetPassUIEvent.resetPassword)
                        }
                    ),
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.auth_email),
                            style = TextStyle(
                                color = SpanishGray,
                                fontFamily = SourceSans,
                                fontSize = 20.sp,
                            )
                        )
                    },
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = "",
                            tint = SpanishGray,
                        )
                    }
                )

                FilledTonalButton(
                    onClick = {
                        viewModel.onEvent(ResetPassUIEvent.resetPassword)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Vermilion,
                        contentColor = WhiteBlue,
                        disabledContainerColor = SpanishGray,
                        disabledContentColor = WhiteBlue,
                    )
                ) {
                    Text(
                        stringResource(id = R.string.auth_reset_password),
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
        },
    )
}

@Preview(showSystemUi = true, showBackground = true, group = "Done")
@Composable
fun ComposableResetPasswordPreview() {
    ResetPasswordScreen(null)
}
