package com.ayoprez.sobuu.presentation.currently_reading

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.presentation.custom_widgets.CustomTopAppBar
import com.ayoprez.sobuu.presentation.custom_widgets.SegmentText
import com.ayoprez.sobuu.presentation.custom_widgets.SegmentedControl
import com.ayoprez.sobuu.presentation.destinations.CommentsScreenDestination
import com.ayoprez.sobuu.presentation.main.toStringDateWithDayAndTime
import com.ayoprez.sobuu.shared.models.bo_models.CurrentlyReadingBook
import com.ayoprez.sobuu.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.LocalDateTime

enum class DialogUpdateProgressType(private val resId: Int) {
    PAGE(R.string.update_book_progress_dialog_page),
    PERCENTAGE(R.string.update_book_progress_dialog_percentage);

    fun getText(context: Context): String {
        return context.getString(resId)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun CurrentlyReadingScreen(
    nav: DestinationsNavigator? = null,
    bookId: String,
    viewModel: CurrentlyReadingViewModel = hiltViewModel(),
) {
    viewModel.onEvent(CurrentlyReadingUIEvent.FetchBookProgressData(bookId = bookId))
    Scaffold(
        topBar = {
            CustomTopAppBar(
                nav = nav,
                text = stringResource(id = R.string.reading),
                titleSize = 24.sp,
            )
        },
        content = {
            CurrentlyReadingContent(
                modifier = Modifier
                    .padding(it)
                    .clickable {
                        if (viewModel.bookData?.bookProgressComments != null) {
                            nav?.navigate(
                                CommentsScreenDestination(
                                    bookId = bookId,
                                    page = viewModel.updatedProgress?.page,
                                    percentage = viewModel.updatedProgress?.percentage,
                                )
                            )
                        }
                    },
                book = viewModel.bookData,
                currentProgress = viewModel.bookData?.bookProgress?.progressInPercentage?.toFloat(),
                currentProgressPage = viewModel.bookData?.bookProgress?.page?.toInt(),
                currentProgressPercentage = viewModel.bookData?.bookProgress?.percentage?.toInt(),
                onUpdateProgressDialogButtonClicked = {
                    viewModel.onEvent(CurrentlyReadingUIEvent.UpdateProgress(
                        bookID = bookId,
                    ))
                },
                onUpdateProgressDialogPageChanged = { page ->
                    viewModel.onEvent(CurrentlyReadingUIEvent.UpdateProgressChanged(
                        percentage = null,
                        page = page,
                    ))
                },
                onUpdateProgressDialogPercentageChanged = { percentage ->
                    viewModel.onEvent(CurrentlyReadingUIEvent.UpdateProgressChanged(
                        page = null,
                        percentage = percentage,
                    ))
                },
            )
        }
    )
}

@Composable
fun CurrentlyReadingContent(
    modifier: Modifier = Modifier,
    book: CurrentlyReadingBook?,
    currentProgressPage: Int?,
    currentProgressPercentage: Int?,
    currentProgress: Float?,
    onUpdateProgressDialogButtonClicked: () -> Unit,
    onUpdateProgressDialogPercentageChanged: (Int?) -> Unit,
    onUpdateProgressDialogPageChanged: (Int?) -> Unit,
) {

    val displayUpdateProgressDialog = remember { mutableStateOf(false) }
    var displayGiveUpDialog by remember { mutableStateOf(false) }
    var displayFinishDialog by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center
    ) {
        UpdateProgressDialog(
            onShowDialogState = displayUpdateProgressDialog,
            currentPage = currentProgressPage,
            currentPercentage = currentProgressPercentage,
            onButtonClick = onUpdateProgressDialogButtonClicked,
            onPercentageChanged = onUpdateProgressDialogPercentageChanged,
            onPageChanged = onUpdateProgressDialogPageChanged,
        )

        Column(
            modifier = Modifier
                .background(WhiteBlue)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BookProgressCard(
                modifier = Modifier
                    .padding(20.dp)
                    .then(modifier),
                picture = book?.picture ?: "",
                progress = currentProgress ?: 0f,
                startedToRead = book?.bookProgress?.startedToRead,
                finishedToRead = null,
                title = book?.title ?: "",
                authors = book?.authors ?: emptyList(),
                finished = false,
                giveUp = false,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                TextButton(
                    modifier = Modifier.padding(start = 10.dp),
                    onClick = {
                        displayUpdateProgressDialog.value = true
                    }
                ) {
                    Text(
                        stringResource(id = R.string.update_progress_button),
                        maxLines = 2,
                        style = TextStyle(
                            fontFamily = SourceSans,
                            fontSize = 18.sp,
                            color = DarkLava,
                        )
                    )
                }

                TextButton(
                    modifier = Modifier.padding(start = 10.dp),
                    onClick = { displayGiveUpDialog = true }
                ) {
                    Text(
                        stringResource(id = R.string.give_up_book_button),
                        maxLines = 1,
                        style = TextStyle(
                            fontFamily = SourceSans,
                            fontSize = 18.sp,
                            color = DarkLava,
                        )
                    )
                }

                TextButton(
                    modifier = Modifier.padding(end = 10.dp),
                    onClick = { displayFinishDialog = true }
                ) {
                    Text(
                        stringResource(id = R.string.finish_book_button),
                        maxLines = 1,
                        style = TextStyle(
                            fontFamily = SourceSans,
                            fontSize = 18.sp,
                            color = DarkLava,
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun BookProgressCard(
    modifier: Modifier = Modifier,
    picture: String,
    progress: Float,
    startedToRead: LocalDateTime?,
    finishedToRead: LocalDateTime?,
    title: String,
    authors: List<String>,
    finished: Boolean,
    giveUp: Boolean,
) {
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp.dp
    val screenWidth = config.screenWidthDp.dp

    Box(
        modifier = Modifier
            .then(modifier)
            .height((70 * screenHeight) / 100)
            .width(screenWidth - 70.dp)
            .border(
                border = BorderStroke(2.dp, color = DarkLava),
                shape = RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(DarkLava)
            .padding(10.dp)
    ) {

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {

            val (bookCover, bookTitle, bookAuthors, startedDate,
                progressIndicator, endDate) = createRefs()

            createVerticalChain(bookTitle, bookAuthors, chainStyle = ChainStyle.Packed)

            Text(
                text = title,
                style = TextStyle(
                    fontSize = 25.sp,
                    color = WhiteBlue,
                    fontFamily = SourceSans,
                    fontWeight = FontWeight.Medium,
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.constrainAs(bookTitle) {
                    start.linkTo(parent.start, margin = 50.dp)
                    end.linkTo(parent.end, margin = 50.dp)
                    centerHorizontallyTo(parent)
                    linkTo(parent.top, bookAuthors.top, bias = 0.05f)
                },
            )
            Text(
                text = authors.joinToString(", "),
                style = TextStyle(
                    fontSize = 20.sp,
                    color = WhiteBlue,
                    fontFamily = SourceSans
                ),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .constrainAs(bookAuthors) {
                        start.linkTo(parent.start, margin = 50.dp)
                        end.linkTo(parent.end, margin = 50.dp)
                        centerHorizontallyTo(parent)
                        linkTo(bookTitle.bottom, parent.bottom, bias = 0.90f)
                    },
            )

            AsyncImage(
                model = picture,
                placeholder = painterResource(id = R.drawable.ic_cover_placeholder),
                contentDescription = null,
                modifier = Modifier
                    .height(screenHeight / 2.5f)
                    .constrainAs(bookCover) {
                        top.linkTo(bookAuthors.bottom, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        centerHorizontallyTo(parent)
                    },
            )
            LinealProgressIndicator(
                progress = progress,
                modifier = Modifier.constrainAs(progressIndicator) {
                    top.linkTo(bookCover.bottom, margin = 20.dp)
                    end.linkTo(parent.end, margin = 50.dp)
                    start.linkTo(parent.start, margin = 50.dp)
                }
            )
            Text(
                "${stringResource(id = R.string.started_book_on)} ${
                    startedToRead?.toStringDateWithDayAndTime() ?: LocalDateTime.now()
                        .toStringDateWithDayAndTime()
                }",
                style = TextStyle(
                    fontFamily = SourceSans,
                    fontSize = 12.sp,
                    color = WhiteBlue,
                ),
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 5.dp)
                    .constrainAs(startedDate) {
                        top.linkTo(progressIndicator.bottom)
                        start.linkTo(progressIndicator.start, margin = 10.dp)
                    },
            )
            if (finishedToRead != null && (finished || giveUp)) {
                Text(
                    "${
                        if (finished) {
                            stringResource(id = R.string.finished_book_on)
                        } else {
                            stringResource(id = R.string.gave_up_book_on)
                        }
                    } ${
                        finishedToRead.toStringDateWithDayAndTime()
                    }",
                    style = TextStyle(
                        fontFamily = SourceSans,
                        fontSize = 12.sp,
                        color = WhiteBlue,
                    ),
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .constrainAs(endDate) {
                            top.linkTo(startedDate.bottom)
                            start.linkTo(startedDate.start)
                        },
                )
            }
        }
    }
}

@Composable
fun LinealProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        val activeProgressBarWidth: Dp = (progress * 250.dp) / 100

        Box(
            modifier = Modifier
                .height(30.dp)
                .width(250.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(GreenSheen),
        )

        Box(
            modifier = Modifier
                .height(30.dp)
                .width(activeProgressBarWidth)
                .clip(RoundedCornerShape(10.dp))
                .background(Vermilion)
                .align(Alignment.CenterStart),
        )

        Text(
            text = "$progress%",
            style = TextStyle(
                color = WhiteBlue,
                fontSize = 18.sp,
                fontFamily = SourceSans,
            )
        )
    }

}

@Composable
fun UpdateProgressDialog(
    modifier: Modifier = Modifier,
    onShowDialogState: MutableState<Boolean> = mutableStateOf(true),
    currentPage: Int?,
    currentPercentage: Int?,
    onPercentageChanged: (Int?) -> Unit,
    onPageChanged: (Int?) -> Unit,
    onButtonClick: () -> Unit,
) {
    val context = LocalContext.current
    val textPercentageState = remember { mutableStateOf(TextFieldValue(
        text = getCurrentProgress(
            currentPage = currentPage,
            currentPercentage = currentPercentage
        )
    )) }
    var displayInvalidNumberError by remember { mutableStateOf(false) }

    val textPageState = remember { mutableStateOf(TextFieldValue(
        text = getCurrentProgress(
            currentPage = currentPage,
            currentPercentage = currentPercentage
        )
    )) }
    val segments = remember {
        listOf(DialogUpdateProgressType.PAGE, DialogUpdateProgressType.PERCENTAGE)
    }
    var type by remember {
        mutableStateOf(
            if (currentPage == null) segments.last() else segments.first()
        )
    }

    if (onShowDialogState.value) {
        Dialog(
            onDismissRequest = { onShowDialogState.value = false },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = true),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .background(WhiteBlue, shape = RoundedCornerShape(8.dp))
                    .padding(10.dp)
                    .then(modifier),
            ) {
                Text(
                    text = stringResource(id = R.string.update_book_progress_dialog_title),
                    style = TextStyle(
                        fontFamily = SourceSans,
                        fontSize = 18.sp,
                        color = DarkLava
                    ),
                )

                if(currentPage == -1 && currentPercentage == -1 || currentPage == null && currentPercentage == null) {
                    SegmentedControl(
                        modifier = Modifier.padding(
                            top = 5.dp,
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 5.dp
                        ),
                        segments = segments,
                        selectedSegment = type,
                        onSegmentSelected = {
                            type = it
                        },
                    ) {
                        SegmentText(
                            it.getText(context = context),
                            style = TextStyle(
                                fontFamily = SourceSans,
                                fontSize = 16.sp,
                                color = DarkLava,
                            )
                        )
                    }
                } else {
                    type = if(currentPage == -1 || currentPage == null) {
                        DialogUpdateProgressType.PERCENTAGE
                    } else {
                        DialogUpdateProgressType.PAGE
                    }
                }

                if (type == DialogUpdateProgressType.PAGE) {
                    BasicTextField(
                        modifier = Modifier
                            .width(IntrinsicSize.Min)
                            .padding(10.dp)
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    val text = textPageState.value.text
                                    textPageState.value = textPageState.value.copy(
                                        selection = TextRange(0, text.length)
                                    )
                                }
                            },
                        value = textPageState.value,
                        maxLines = 1,
                        onValueChange = {
                            displayInvalidNumberError = false
                            textPageState.value = it
                            textPercentageState.value = TextFieldValue("")
                            if(it.text.isNumber() && it.text.toInt() != null) {
                                onPageChanged.invoke(it.text.toInt()!!)
                            } else {
                                displayInvalidNumberError = true
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        textStyle = TextStyle(
                            fontSize = 80.sp,
                            fontFamily = SourceSans,
                            color = DarkLava,
                        )
                    )
                } else {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        BasicTextField(
                            modifier = Modifier
                                .width(IntrinsicSize.Min)
                                .onFocusChanged { focusState ->
                                    if (focusState.isFocused) {
                                        val text = textPercentageState.value.text
                                        textPercentageState.value = textPercentageState.value.copy(
                                            selection = TextRange(0, text.length)
                                        )
                                    }
                                },
                            value = textPercentageState.value,
                            maxLines = 1,
                            onValueChange = {
                                displayInvalidNumberError = false
                                textPercentageState.value = it
                                textPageState.value = TextFieldValue("")
                                if(it.text.isNumber() && it.text.toInt() != null) {
                                    onPercentageChanged.invoke(it.text.toInt()!!)
                                } else {
                                    displayInvalidNumberError = true
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            textStyle = TextStyle(
                                fontSize = 80.sp,
                                fontFamily = SourceSans,
                                color = DarkLava,
                            )
                        )
                        Text(
                            modifier = Modifier.padding(start = 10.dp),
                            text = "%",
                            style = TextStyle(
                                fontSize = 70.sp,
                                fontFamily = SourceSans,
                                color = DarkLava,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    }
                }

                if(displayInvalidNumberError) {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = stringResource(id = R.string.error_not_valid_number_in_book_progress_dialogn),
                        style = TextStyle(
                            color = Vermilion,
                            fontSize = 14.sp,
                            fontFamily = SourceSans,
                        ),
                    )
                }

                FilledTonalButton(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .padding(bottom = 10.dp, top = if(displayInvalidNumberError) 0.dp else 50.dp),
                    onClick = {
                        onButtonClick.invoke()
                        onShowDialogState.value = false
                    },
                    enabled = !displayInvalidNumberError,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Vermilion,
                        contentColor = WhiteBlue,
                        disabledContainerColor = SpanishGray,
                        disabledContentColor = WhiteBlue,
                    )
                ) {
                    Text(
                        stringResource(id = R.string.update_book_progress_dialog_button),
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = WhiteBlue,
                            fontWeight = FontWeight.Normal,
                            fontFamily = SourceSans
                        ),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

private fun getCurrentProgress(currentPage: Int?, currentPercentage: Int?): String {
    return if(currentPage == -1 && currentPercentage == -1) {
        "0"
    } else currentPage?.toString() ?: (currentPercentage?.toString() ?: "")
}

@Preview(group = "Done")
@Composable
fun BookProgressCardPreview() {
    BookProgressCard(
        picture = "",
        progress = 23f,
        startedToRead = LocalDateTime.now(),
        title = "1984",
        authors = listOf("George Orwell"),
        finished = false,
        giveUp = false,
        finishedToRead = null,
    )
}

@Preview(group = "Done")
@Composable
fun LinealProgressIndicatorPreview() {
    LinealProgressIndicator(progress = 68.77f)
}

@Preview(group = "Done")
@Composable
fun DialogUpdateProgressPreview() {
    UpdateProgressDialog(
        currentPage = null,
        currentPercentage = 100,
        onButtonClick = {},
        onPageChanged = {},
        onPercentageChanged = {}
    )
}

fun String.isNumber(): Boolean {
    return this.toIntOrNull() != null
}

fun String.toInt(): Int? {
    return this.toIntOrNull()
}