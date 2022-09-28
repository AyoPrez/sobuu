package com.ayoprez.sobuu.presentation.custom_widgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ayoprez.sobuu.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopRoundedOutlinedTextField(
    fieldValue: String,
    onFieldValueChange: (String) -> Unit,
    placeholderText: String,
    icon: ImageVector,
    isError: Boolean = false,
) {
    val focusManager = LocalFocusManager.current

    ProvideTextStyle(value = TextStyle(color = DarkLava)) {
        OutlinedTextField(
            value = fieldValue,
            onValueChange = onFieldValueChange,
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
                errorBorderColor = Vermilion,
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            isError = isError,
            placeholder = {
                Text(
                    text = placeholderText,
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
                    imageVector = icon,
                    contentDescription = "",
                    tint = SpanishGray,
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun BottomRoundedOutlinedTextField(
    fieldValue: String,
    onFieldValueChange: (String) -> Unit,
    placeholderText: String,
    icon: ImageVector,
    onKeyboardActionClicked: () -> Unit,
    passwordField: Boolean = false,
    isError: Boolean = false,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    ProvideTextStyle(value = TextStyle(color = DarkLava)) {
        OutlinedTextField(
            value = fieldValue,
            onValueChange = onFieldValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(
                        bottomStart = 10.dp,
                        bottomEnd = 10.dp,
                    ),
                    color = WhiteBlue,
                ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = DarkLava,
                unfocusedBorderColor = DarkLava,
                errorBorderColor = Vermilion,
            ),
            isError = isError,
            placeholder = {
                Text(
                    placeholderText,
                    style = TextStyle(
                        color = SpanishGray,
                        fontFamily = SourceSans,
                        fontSize = 20.sp,
                    )
                )
            },
            visualTransformation = if (passwordField) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Go,
                keyboardType = if (passwordField) KeyboardType.Password else KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    onKeyboardActionClicked()
                    keyboardController?.hide()
                }
            ),
            shape = RoundedCornerShape(
                bottomStart = 10.dp,
                bottomEnd = 10.dp,
            ),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    tint = SpanishGray
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CompleteRoundedOutlineTextField(
    fieldValue: String,
    onFieldValueChange: (String) -> Unit,
    placeholderText: String,
    icon: ImageVector,
    onKeyboardActionClicked: () -> Unit,
    isError: Boolean = false,
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    ProvideTextStyle(value = TextStyle(color = DarkLava)) {
        OutlinedTextField(
            value = fieldValue,
            onValueChange = onFieldValueChange,
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
                errorBorderColor = Vermilion,
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
            keyboardActions = KeyboardActions(
                onNext = {
                    onKeyboardActionClicked()
                    keyboardController?.hide()
                }
            ),
            isError = isError,
            placeholder = {
                Text(
                    text = placeholderText,
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
                    imageVector = icon,
                    contentDescription = "",
                    tint = SpanishGray,
                )
            }
        )
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun NotRoundedOutlineTextField(
    fieldValue: String,
    onFieldValueChange: (String) -> Unit,
    placeholderText: String,
    icon: ImageVector,
    passwordField: Boolean = false,
    isError: Boolean,
) {
    val focusManager = LocalFocusManager.current
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    ProvideTextStyle(value = TextStyle(color = DarkLava)) {
        OutlinedTextField(
            value = fieldValue,
            onValueChange = onFieldValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(0.dp),
                    color = WhiteBlue
                )
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = DarkLava,
                unfocusedBorderColor = DarkLava,
                errorBorderColor = Vermilion,
            ),
            visualTransformation = if (passwordField) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = if (passwordField) KeyboardType.Password else KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            isError = isError,
            placeholder = {
                Text(
                    text = placeholderText,
                    style = TextStyle(
                        color = SpanishGray,
                        fontFamily = SourceSans,
                        fontSize = 20.sp,
                    )
                )
            },
            shape = RoundedCornerShape(0.dp),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    tint = SpanishGray,
                )
            }
        )
    }
}