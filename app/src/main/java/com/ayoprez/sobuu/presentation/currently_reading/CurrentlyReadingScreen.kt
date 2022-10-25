package com.ayoprez.sobuu.presentation.currently_reading

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.presentation.custom_widgets.CustomTopAppBar
import com.ayoprez.sobuu.presentation.destinations.CommentsScreenDestination
import com.ayoprez.sobuu.presentation.main.toStringDateWithDayAndTime
import com.ayoprez.sobuu.shared.models.bo_models.CurrentlyReadingBook
import com.ayoprez.sobuu.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun CurrentlyReadingScreen(
    nav: DestinationsNavigator? = null,
    book: CurrentlyReadingBook,
    viewModel: CurrentlyReadingViewModel = hiltViewModel(),
) {
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
                        if (book.bookProgressComments != null) {
                            nav?.navigate(CommentsScreenDestination(
                                bookId = book.id,
                                page = book.bookProgress?.page,
                                percentage = book.bookProgress?.percentage,
                            ))
                        }
                    },
                book = book,
            )
        }
    )
}

@Composable
fun CurrentlyReadingContent(
    modifier: Modifier = Modifier,
    book: CurrentlyReadingBook,
) {
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
            picture = book.picture,
            progress = book.bookProgress?.progressInPercentage?.toFloat() ?: 0f,
            startedToRead = book.bookProgress?.startedToRead,
            finishedToRead = null,
            title = book.title,
            authors = book.authors,
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
                onClick = { /*TODO*/ }
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
                onClick = { /*TODO*/ }
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
                onClick = { /*TODO*/ }
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
                    fontSize = 20.sp,
                    color = WhiteBlue,
                    fontFamily = SourceSans
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.constrainAs(bookTitle) {
                    start.linkTo(parent.start, margin = 50.dp)
                    end.linkTo(parent.end, margin = 50.dp)
                    centerHorizontallyTo(parent)
                    linkTo(parent.top, bookAuthors.top, bias = 0.10f)
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
                    .height(screenHeight / 3)
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
                    top.linkTo(bookCover.bottom, margin = 10.dp)
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
                .height(20.dp)
                .width(250.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(GreenSheen),
        )

        Box(
            modifier = Modifier
                .height(20.dp)
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

@Preview
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

@Preview
@Composable
fun LinealProgressIndicatorPreview() {
    LinealProgressIndicator(progress = 68.77f)
}