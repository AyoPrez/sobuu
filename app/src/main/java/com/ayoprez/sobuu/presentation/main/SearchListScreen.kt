package com.ayoprez.sobuu.presentation.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.presentation.custom_widgets.IconAndText
import com.ayoprez.sobuu.presentation.destinations.BookScreenDestination
import com.ayoprez.sobuu.shared.features.book.remote.BookError
import com.ayoprez.sobuu.ui.theme.*
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun SearchListScreen(
    nav: DestinationsNavigator? = null,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val bookList = viewModel.booksList
    val context = LocalContext.current

    LazyColumn(contentPadding = PaddingValues(5.dp)) {
        items(bookList?.size ?: 0) { position ->
            val book = bookList?.get(position) ?: return@items
            BookListItem(
                title = book.title,
                author = book.authors[0],
                totalPages = book.totalPages,
                description = book.description,
                cover = book.picture,
                totalReviews = book.bookRating.size,
                totalComments = book.bookComments.size,
                modifier = Modifier.clickable {
                    nav?.navigate(
                        BookScreenDestination(book)
                    )
                }
            )
        }
    }

    if(state.isLoading) {
        SearchScreenLoading()
    }

    if(state.error != null) {
        Spacer(modifier = Modifier.height(25.dp))

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
    }
}

@Composable
fun SearchScreenLoading(
    modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBlue),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(modifier = modifier, color = GreenSheen)
    }
}

@Composable
fun getStringFromError(error: BookError?): String {
    return when (error) {
        is BookError.EmptySearchTermError -> {
            stringResource(id = R.string.error_empty_term)
        }
        is BookError.TimeOutError -> {
            stringResource(id = R.string.error_timeout)
        }
        else -> {
            stringResource(id = R.string.error_unknown)
        }
    }
}

@Composable
fun BookListItem(
    peopleReadingTheBook: Int = 0,
    title: String,
    author: String,
    totalPages: Int,
    totalReviews: Int = 0,
    totalComments: Int = 0,
    rate: Double = 0.0,
    description: String,
    cover: String,
    userHasReadTheBook: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .background(WhiteBlue)
            .fillMaxWidth()
            .height(250.dp)
            .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
            .border(
                border = BorderStroke(2.dp, color = SpanishGray),
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp))
            .composed { modifier },
    ) {

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {

            val (bookCover, bottomBox, bookTitle, bookAuthors,
                bookDescription, bookPages, bookRead, peopleReading,
                bookComments, bookReviews, bookRating) = createRefs()
            val verticalMiddleGuideline = createGuidelineFromTop(0.3f)

            Text(
                text = if (userHasReadTheBook)
                    stringResource(id = R.string.user_has_read_the_book)
                else
                    stringResource(id = R.string.user_has_not_read_the_book),
                style = TextStyle(
                    fontFamily = SourceSans,
                    color = DarkLava,
                    fontSize = 12.sp,
                ),
                modifier = Modifier
                    .constrainAs(bookRead) {
                        top.linkTo(parent.top, margin = 5.dp)
                        bottom.linkTo(bookRating.top, margin = 4.dp)
                        start.linkTo(bookCover.end, margin = 4.dp)
                        end.linkTo(peopleReading.start, margin = 4.dp)
                    },
            )

            IconAndText(
                text = when {
                    peopleReadingTheBook == 0 -> stringResource(id = R.string.none_reading_book)
                    peopleReadingTheBook == 1 -> "$peopleReadingTheBook ${stringResource(id = R.string.one_person_reading_book)}"
                    peopleReadingTheBook > 999 -> "${stringResource(id = R.string.more_than_999)} ${
                        stringResource(
                            id = R.string.plural_persons_reading_book
                        )
                    }"
                    else -> "$peopleReadingTheBook ${stringResource(id = R.string.plural_persons_reading_book)}"
                },
                fontSize = 12.sp,
                textColor = DarkLava,
                customIcon = { CurrentlyReadingIcon() },
                modifier = Modifier
                    .constrainAs(peopleReading) {
                        top.linkTo(bookRead.top)
                        bottom.linkTo(bookRead.bottom)
                        linkTo(bookRead.end, parent.end, startMargin = 4.dp, endMargin = 4.dp, bias = 1F)
                    },
            )

            IconAndText(
                text = rate.toString(),
                fontSize = 18.sp,
                icon = Icons.Filled.Star,
                modifier = Modifier
                    .constrainAs(bookRating) {
                        top.linkTo(bookRead.bottom)
                        bottom.linkTo(verticalMiddleGuideline)
                        start.linkTo(bookCover.end, 4.dp)
                        linkTo(bookRead.bottom, bottomBox.top, topMargin = 2.dp, bottomMargin = 2.dp, bias = 1F)
                    },
            )

            Text(
                text = when {
                    totalReviews == 1 -> "$totalReviews ${stringResource(id = R.string.review)}"
                    totalReviews > 999 -> "${stringResource(id = R.string.more_than_999)} ${
                        stringResource(
                            id = R.string.reviews
                        )
                    }"
                    else -> "$totalReviews ${stringResource(id = R.string.reviews)}"
                },
                style = TextStyle(
                    fontFamily = SourceSans,
                    color = DarkLava,
                    fontSize = 14.sp,
                ),
                modifier = Modifier
                    .constrainAs(bookReviews) {
                        top.linkTo(bookRating.top)
                        bottom.linkTo(bookRating.bottom)
                        start.linkTo(bookRating.end, 2.dp)
                        end.linkTo(bookComments.start, 4.dp)
                    }
            )

            IconAndText(
                text = when {
                    totalComments == 1 -> "$totalComments ${stringResource(id = R.string.comment)}"
                    totalComments > 999 -> "${stringResource(id = R.string.more_than_999)} ${
                        stringResource(
                            id = R.string.comments
                        )
                    }"
                    else -> "$totalComments ${stringResource(id = R.string.comments)}"
                },
                fontSize = 14.sp,
                icon = Icons.Filled.Comment,
                iconColor = DarkLava,
                modifier = Modifier
                    .constrainAs(bookComments) {
                        top.linkTo(bookRating.top)
                        bottom.linkTo(bookRating.bottom)
                        start.linkTo(bookReviews.end)
                        end.linkTo(parent.end)
                        linkTo(
                            bookReviews.end,
                            parent.end,
                            startMargin = 4.dp,
                            endMargin = 4.dp,
                            bias = 1F
                        )
                    },
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp))
                    .padding(1.dp)
                    .background(SpanishGray)
                    .constrainAs(bottomBox) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        top.linkTo(verticalMiddleGuideline)
                        height = Dimension.fillToConstraints
                    },
            )

            Text(
                text = title,
                style = TextStyle(
                    fontFamily = SourceSans,
                    color = WhiteBlue,
                    fontSize = 18.sp,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(bookTitle) {
                    top.linkTo(bottomBox.top, 4.dp)
                    end.linkTo(bottomBox.end, 4.dp)
                    start.linkTo(bookCover.end, 4.dp)
                    width = Dimension.fillToConstraints
                }
            )

            Text(
                text = author,
                style = TextStyle(
                    fontFamily = SourceSans,
                    color = DarkLava,
                    fontSize = 16.sp,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(bookAuthors) {
                    top.linkTo(bookTitle.bottom, 4.dp)
                    end.linkTo(bottomBox.end, 4.dp)
                    start.linkTo(bookCover.end, 4.dp)
                    width = Dimension.fillToConstraints
                }
            )
            Text(
                text = description,
                style = TextStyle(
                    fontFamily = SourceSans,
                    color = WhiteBlue,
                    fontSize = 14.sp,
                ),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(bookDescription) {
                    top.linkTo(bookAuthors.bottom, 4.dp)
                    end.linkTo(bottomBox.end, 4.dp)
                    start.linkTo(bookCover.end, 4.dp)
                    bottom.linkTo(bottomBox.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            )

            Column(
                modifier = Modifier.constrainAs(bookCover) {
                start.linkTo(parent.start, 10.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom, 5.dp)
                width = Dimension.percent(0.2f)
                linkTo(
                    parent.top,
                    parent.bottom,
                    topMargin = 5.dp,
                    bottomMargin = 5.dp,
                    bias = 0.1f
                )
            },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AsyncImage(
                    model = "https://books.google.com/books/content?id=ImAGEAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api",
//                    model = cover,
                    placeholder = painterResource(id = R.drawable.ic_cover_placeholder),
                    contentDescription = null,
                    modifier = Modifier
                        .width(74.dp)
                        .height(139.dp)
                )

                IconAndText(
                    text = totalPages.toString(),
                    fontSize = 14.sp,
                    iconColor = WhiteBlue,
                    textColor = WhiteBlue,
                    iconPainter = painterResource(id = R.drawable.ic_book_open_page_variant),
                )
            }
        }
    }
}

@Composable
fun CalculateRateIcons(
    rate: Double,
    modifier: Modifier = Modifier
) {
    LazyRow(modifier = modifier) {
        items(5) { index ->
            if (rate > index) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    modifier = Modifier.size(12.dp),
                    contentDescription = null,
                    tint = DarkLava,
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.StarBorder,
                    modifier = Modifier.size(12.dp),
                    contentDescription = null,
                    tint = DarkLava,
                )
            }
        }
    }
}

@Composable
fun CurrentlyReadingIcon(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        Icon(
            painterResource(id = R.drawable.ic_book_open_variant),
            modifier = Modifier.size(18.dp),
            contentDescription = null,
            tint = Vermilion,
        )
        Icon(
            painterResource(id = R.drawable.ic_account_group),
            modifier = Modifier.size(16.dp),
            contentDescription = null,
            tint = GreenSheen,
        )
    }
}

@Preview(group = "Done", locale = "es")
@Composable
fun BookListItemPreview() {
    BookListItem(
        peopleReadingTheBook = 5555,
        title = "El imperio final",
        author = "Brandon Sanderson",
        totalPages = 654,
        rate = 2.5,
        totalReviews = 542213,
        totalComments = 465132465,
        description = "Durante mil años han caído cenizas del cielo. Durante mil años nada ha florecido. Durante mil años los skaa han sido esclavizados y viven en la miseria, sumidos en un miedo inevitable. Durante mil años...",
        cover = "https://images-eu.ssl-images-amazon.com/images/I/51wrYVDxFNS._SY264_BO1,204,203,200_QL40_ML2_.jpg",
    )
}

@Preview(group = "Done")
@Composable
fun RatingCalculationPreview() {
    CalculateRateIcons(rate = 2.5)
}

@Preview(group = "Done")
@Composable
fun CurrentlyReadingIconPreview() {
    CurrentlyReadingIcon()
}