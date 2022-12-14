package com.ayoprez.sobuu.presentation.book

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.presentation.custom_widgets.Chip
import com.ayoprez.sobuu.presentation.custom_widgets.IconAndText
import com.ayoprez.sobuu.presentation.custom_widgets.MenuItemData
import com.ayoprez.sobuu.presentation.custom_widgets.TopAppBarWithMenu
import com.ayoprez.sobuu.presentation.destinations.BookCoverScreenDestination
import com.ayoprez.sobuu.presentation.destinations.HomeScreenDestination
import com.ayoprez.sobuu.shared.features.book.remote.BookError
import com.ayoprez.sobuu.shared.models.bo_models.Book
import com.ayoprez.sobuu.shared.models.bo_models.BookReadingStatus
import com.ayoprez.sobuu.shared.models.bo_models.Profile
import com.ayoprez.sobuu.shared.models.bo_models.UserBookRating
import com.ayoprez.sobuu.ui.theme.*
import com.google.accompanist.flowlayout.FlowRow
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import com.gowtham.ratingbar.RatingBarStyle
import com.gowtham.ratingbar.StepSize
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
// TODO When the user start a new book, the app should confirm it and jump to home screen
enum class ISBNType {
    ISBN10, ISBN13
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun BookScreen(
    nav: DestinationsNavigator? = null,
    book: Book,
    bookViewModel: BookViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            TopAppBarWithMenu(
                nav = nav,
                listItems = listOf(
                    MenuItemData(
                        text = stringResource(id = R.string.add_to_shelf),
                        icon = Icons.Filled.Add,
                        action = {
                            bookViewModel.onEvent(BookUIEvent.DisplayUserShelves)
                        }
                    ),
                    MenuItemData(
                        text = stringResource(id = R.string.start_to_read),
                        icon = Icons.Filled.ImportContacts,
                        action = {
                            bookViewModel.onEvent(
                                BookUIEvent.StartToReadBook(
                                    bookId = book.id,
                                    bookTitle = book.title,
                                )
                            )
                        },
                        displayIt = book.readingStatus != BookReadingStatus.READING,
                    ),
                    MenuItemData(
                        text = stringResource(id = R.string.search_in_map),
                        icon = Icons.Filled.Map,
                        action = {

                        }
                    )
                )
            )
        },
        content = {
            BookScreenContent(
                nav = nav,
                modifier = Modifier.padding(it),
                peopleReadingIt = book.peopleReadingIt,
                cover = book.picture,
                title = book.title,
                authors = book.authors.joinToString(", "),
                description = book.description,
                credits = book.credits?.joinToString(", ") ?: "",
                publisher = book.publisher,
                publishedDate = book.publishedDate,
                totalPages = book.totalPages.toString(),
                isbn10 = book.isbn[0],
                isbn13 = book.isbn[1],
                genres = book.genres,
                userHasReadTheBook = book.readingStatus,
                totalComments = book.totalComments,
                rating = book.totalRating,
                reviews = book.allReviews,
                userReview = book.userRating,
            )

            if (bookViewModel.state.isLoading) {
                Dialog(
                    onDismissRequest = { !bookViewModel.state.isLoading },
                    DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(100.dp)
                            .background(WhiteBlue, shape = RoundedCornerShape(8.dp))
                            .padding(20.dp)
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            if (bookViewModel.state.displayStartedToRead) {
                SuccessStartToReadDialog(
                    isLoading = !bookViewModel.state.isLoading,
                    bookTitle = bookViewModel.state.bookStartedReadingTitle ?: "",
                    onClickButton = {
                        nav?.navigate(HomeScreenDestination)
                    }
                )
            }

            if (bookViewModel.state.error != null) {
                AlertDialog(
                    title = {
                        Text(
                            text = stringResource(id = R.string.error_title_book_details),
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = SourceSans,
                                color = DarkLava
                            )
                        )
                    },
                    text = {
                        Text(
                            text = getStringFromBookError(bookViewModel.state.error),
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = SourceSans,
                                color = DarkLava
                            )
                        )
                    },
                    containerColor = WhiteBlue,
                    textContentColor = DarkLava,
                    confirmButton = {
                        TextButton(
                            onClick = {
                                bookViewModel.onEvent(BookUIEvent.CancelAllErrors)
                            }) {
                            Text(stringResource(id = R.string.ok))
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    onDismissRequest = {}
                )
            }
        }
    )
}

@Composable
fun BookScreenContent(
    nav: DestinationsNavigator?,
    modifier: Modifier = Modifier,
    peopleReadingIt: Int,
    cover: String,
    title: String,
    authors: String,
    description: String,
    credits: String,
    publisher: String,
    publishedDate: String,
    totalPages: String,
    isbn10: String?,
    isbn13: String?,
    genres: List<String>,
    userHasReadTheBook: BookReadingStatus,
    totalComments: Int,
    rating: Double,
    reviews: List<UserBookRating>,
    userReview: UserBookRating? = null,
) {
    Column(
        modifier = Modifier
            .background(WhiteBlue)
            .padding(12.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = cover,
                placeholder = painterResource(id = R.drawable.ic_cover_placeholder),
                contentDescription = null,
                modifier = Modifier
                    .width(120.dp)
                    .height(200.dp)
                    .clickable {
                        nav?.navigate(
                            BookCoverScreenDestination(
                                cover = cover,
                            )
                        )
                    },
            )
//            PeopleReadingItSign(
//                peopleReadingIt = peopleReadingIt
//            )
        }

        Text(
            title,
            modifier = Modifier
                .padding(5.dp),
            style = TextStyle(
                fontFamily = SourceSans,
                color = DarkLava,
                fontSize = 20.sp,
            ),
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            authors,
            modifier = Modifier
                .padding(5.dp),
            style = TextStyle(
                fontFamily = SourceSans,
                color = DarkLava,
                fontSize = 14.sp,
            ),
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
        )

        if (credits.isNotBlank()) {
            IconAndText(
                text = credits,
                fontSize = 12.sp,
                icon = Icons.Filled.Translate,
                modifier = Modifier
                    .padding(top = 15.dp, bottom = 5.dp, start = 5.dp, end = 5.dp),
            )
        }

        Row(modifier = Modifier.padding(top = 10.dp)) {
            IconAndText(
                text = publisher,
                fontSize = 16.sp,
                icon = Icons.Filled.Domain,
                modifier = Modifier.padding(start = 5.dp, end = 5.dp),
            )
            IconAndText(
                text = publishedDate,
                fontSize = 16.sp,
                icon = Icons.Filled.CalendarToday,
                modifier = Modifier.padding(start = 5.dp, end = 5.dp),
            )
            IconAndText(
                text = totalPages,
                fontSize = 16.sp,
                iconPainter = painterResource(id = R.drawable.ic_book_open_page_variant),
                modifier = Modifier.padding(start = 5.dp, end = 5.dp),
            )
        }

        Row(modifier = Modifier.padding(bottom = 10.dp)) {
            if (!isbn10.isNullOrBlank()) {
                IconAndText(
                    text = isbn10,
                    fontSize = 16.sp,
                    customIcon = { CreateISBNIcon(type = ISBNType.ISBN10) },
                    modifier = Modifier
                        .padding(top = 5.dp, start = 5.dp, end = 5.dp),
                )
            }
            if (!isbn13.isNullOrBlank()) {
                IconAndText(
                    text = isbn13,
                    fontSize = 16.sp,
                    customIcon = { CreateISBNIcon(type = ISBNType.ISBN13) },
                    modifier = Modifier
                        .padding(top = 5.dp, start = 5.dp, end = 5.dp),
                )
            }
        }

        Text(
            description,
            modifier = Modifier
                .padding(5.dp),
            style = TextStyle(
                fontFamily = SourceSans,
                color = DarkLava,
                fontSize = 14.sp,
            ),
            textAlign = TextAlign.Justify,
        )

        CreateGenresChips(genres = genres)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,

            ) {
            Text(
                text = when (userHasReadTheBook) {
                    BookReadingStatus.NOT_READ -> stringResource(id = R.string.user_has_not_read_the_book)
                    BookReadingStatus.READING -> stringResource(id = R.string.user_is_reading_the_book)
                    BookReadingStatus.FINISHED -> stringResource(id = R.string.user_has_read_the_book)
                    BookReadingStatus.GIVE_UP -> stringResource(id = R.string.user_gave_up_with_the_book)
                },
                style = TextStyle(
                    fontFamily = SourceSans,
                    color = DarkLava,
                    fontSize = 12.sp,
                ),
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
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, bottom = 10.dp)
                .clickable {

                },
            horizontalArrangement = Arrangement.Center,
        ) {
            CalculateRateIcons(rate = rating, color = DarkLava)

            Text(
                text = when {
                    reviews.size == 1 -> "${reviews.size} ${stringResource(id = R.string.review)}"
                    reviews.size > 999 -> "${stringResource(id = R.string.more_than_999)} ${
                        stringResource(
                            id = R.string.reviews
                        )
                    }"
                    else -> "${reviews.size} ${stringResource(id = R.string.reviews)}"
                },
                modifier = Modifier.padding(start = 10.dp),
                style = TextStyle(
                    fontFamily = SourceSans,
                    color = DarkLava,
                    fontSize = 16.sp,
                ),
            )
        }

        if (userReview != null) {
            ReviewItem(review = userReview)
        } else {
            Spacer(Modifier.height(30.dp))
        }
    }
}

@Composable
fun CreateISBNIcon(
    type: ISBNType,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Icon(
            painter = painterResource(id = R.drawable.ic_code_bar),
            tint = DarkLava,
            modifier = Modifier.size(16.dp),
            contentDescription = null,
        )
        Text(
            text = if (type == ISBNType.ISBN10) "10" else "13",
            style = TextStyle(
                fontFamily = SourceSans,
                color = GreenSheen.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            ),
        )
    }
}

@Composable
fun PeopleReadingItSign(
    modifier: Modifier = Modifier,
    peopleReadingIt: Int
) {
    Row(
        modifier = Modifier
            .background(SpanishGray)
            .width(70.dp)
            .height(20.dp)
            .offset(x = (-10).dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_currently_reading),
            contentDescription = null,
            modifier = Modifier.padding(start = 12.dp),
        )
        Text(
            text = when {
                peopleReadingIt == 0 -> stringResource(id = R.string.none_reading_book)
                peopleReadingIt == 1 -> "$peopleReadingIt ${stringResource(id = R.string.one_person_reading_book)}"
                peopleReadingIt > 999 -> "${stringResource(id = R.string.more_than_999)} ${
                    stringResource(
                        id = R.string.plural_persons_reading_book
                    )
                }"
                else -> "$peopleReadingIt ${stringResource(id = R.string.plural_persons_reading_book)}"
            },
            modifier = Modifier.padding(start = 12.dp, end = 12.dp),
            style = TextStyle(
                color = WhiteBlue,
                fontSize = 10.sp,
                fontFamily = SourceSans
            ),

            maxLines = 2,
        )
    }
}

@Composable
fun CalculateRateIcons(
    modifier: Modifier = Modifier,
    rate: Double,
    color: Color,
    starSize: Dp = 24.dp,
) {
    RatingBar(
        modifier = modifier,
        value = rate.toFloat(),
        config = RatingBarConfig()
            .activeColor(color)
            .hideInactiveStars(false)
            .inactiveColor(WhiteBlue)
            .stepSize(StepSize.HALF)
            .numStars(5)
            .isIndicator(true)
            .size(starSize)
            .padding(2.dp)
            .style(RatingBarStyle.HighLighted),
        onValueChange = {},
        onRatingChanged = {}
    )
}

@Composable
fun ReviewItem(
    review: UserBookRating,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkLava)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "${review.user?.firstName ?: ""} ${review.user?.lastName ?: ""}",
                modifier = Modifier.padding(start = 5.dp),
                style = TextStyle(
                    fontFamily = SourceSans,
                    color = Vermilion,
                    fontSize = 12.sp,
                ),
            )

            CalculateRateIcons(
                modifier = Modifier.padding(5.dp),
                starSize = 12.dp,
                rate = review.rating,
                color = Vermilion
            )

            Text(
                text = review.date.format(DateTimeFormatter.ofPattern("dd MMMM, yyyy, HH:mm")),
                modifier = Modifier.padding(end = 5.dp),
                style = TextStyle(
                    fontFamily = SourceSans,
                    color = WhiteBlue,
                    fontSize = 12.sp,
                ),
            )
        }

        Text(
            text = review.review,
            modifier = Modifier.padding(10.dp),
            style = TextStyle(
                fontFamily = SourceSans,
                color = WhiteBlue,
                fontSize = 16.sp,
            ),
        )
    }
}

@Composable
fun CreateGenresChips(
    genres: List<String>,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        for (element in genres) {
            Chip(
                text = element,
                backgroundColor = GreenSheen,
                modifier = Modifier.padding(4.dp)
            )
        }
    }

}

@Composable
fun SuccessStartToReadDialog(isLoading: Boolean, bookTitle: String, onClickButton: () -> Unit) {
    var showDialog by remember { mutableStateOf(isLoading) }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .background(WhiteBlue, shape = RoundedCornerShape(8.dp))
                    .padding(20.dp),
            ) {
                BookAnimation()

                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "${stringResource(id = R.string.started_read_successfully)} '$bookTitle'",
                    style = TextStyle(
                        fontFamily = SourceSans,
                        fontSize = 18.sp,
                        color = DarkLava
                    ),
                )

                FilledTonalButton(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp)),
                    onClick = {
                        showDialog = false
                        onClickButton.invoke()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Vermilion,
                        contentColor = WhiteBlue,
                        disabledContainerColor = SpanishGray,
                        disabledContentColor = WhiteBlue,
                    )
                ) {
                    Text(
                        stringResource(id = R.string.started_read_successfully_button),
                        style = TextStyle(
                            fontSize = 16.sp,
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

@Composable
private fun getStringFromBookError(error: BookError?): String {
    if(error == null) return ""
    return when (error) {
        BookError.InvalidBookIdError -> stringResource(id = R.string.error_invalid_book_id_book_details)
        BookError.InvalidDoubleValueError,
        BookError.InvalidFinishedAndGiveUpBookValuesError,
        BookError.InvalidPageNumberError,
        BookError.InvalidPercentageNumberError -> stringResource(id = R.string.error_invalid_progress_book_details)
        BookError.InvalidRateIdError,
        BookError.InvalidRateNumberError -> stringResource(id = R.string.error_invalid_rate_book_details)
        BookError.InvalidSessionTokenError -> stringResource(id = R.string.error_session_token_book_details)
        BookError.ProcessingQueryError -> stringResource(id = R.string.error_processing_query_book_details)
        BookError.TimeOutError -> stringResource(id = R.string.error_timeout_book_details)
        BookError.UnauthorizedQueryError -> stringResource(id = R.string.error_unauthorized_book_details)
        else -> stringResource(id = R.string.error_unknown_book_details)
    }
}

@Composable
fun BookAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.book_animation))
    val progress by animateLottieCompositionAsState(composition)

    LottieAnimation(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp),
        composition = composition,
        progress = { progress },
    )
}

@Preview(showSystemUi = true, showBackground = true, group = "Done")
@Composable
fun BookScreenContentPreview() {
    BookScreenContent(
        nav = null,
        peopleReadingIt = 56,
        cover = "",
        title = "El imperio final",
        authors = "Brandon Sanderson",
        description = "Durante mil a??os han ca??do cenizas del cielo. Durante mil a??os nada ha " +
                "florecido. Durante mil a??os los skaa han sido esclavizados y viven en la miseria, " +
                "sumidos en un miedo inevitable. Durante mil a??os el Lord Legislador ha reinado con " +
                "poder absoluto, dominando gracias al terror, a sus poderes y a su inmortalidad, " +
                "ayudado por ??obligadores?? e ??inquisidores??, junto a la poderosa magia de la " +
                "alomancia.\n" +
                "\n" +
                "Pero los nobles a menudo han tenido trato sexual con j??venes skaa y, aunque la " +
                "ley lo proh??be, algunos de sus bastardos han sobrevivido y heredado los poderes " +
                "alom??nticos: son los ??Nacidos de la Bruma?? (Mistborn).\n" +
                "\n" +
                "Ahora, Kelsier, el ??superviviente??, el ??nico que ha logrado huir de los " +
                "Pozos de Hathsin, ha encontrado a Vin, una pobre chica skaa con mucha suerte... " +
                "Tal vez los dos, con el mejor equipo criminal jam??s reunido, unidos a la rebeli??n " +
                "que los skaa intentan desde hace mil a??os, logren cambiar el mundo y acabar con " +
                "la atroz mano de hierro del Lord Legislador.",
        credits = "Armando Fuerte Bulla",
        publisher = "Nova",
        publishedDate = "2006",
        totalPages = "623",
        isbn10 = "5746854987",
        isbn13 = "1698465465475",
        genres = listOf(
            "Fantasy",
            "Science fiction",
            "Contemporary fiction",
            "Horror",
            "Fiction",
            "Middle Age"
        ),
        userHasReadTheBook = BookReadingStatus.FINISHED,
        totalComments = 211,
        rating = 2.5,
        reviews = emptyList(),
        userReview = UserBookRating(
            id = "",
            rating = 4.5,
            review = "I love it.",
            book = null,
            user = Profile(
                id = "",
                firstName = "Paco",
                lastName = "Jones",
                bookProgress = emptyList(),
                giveUp = emptyList(),
                following = emptyList(),
                alreadyRead = emptyList(),
                userShelves = emptyList(),
            ),
            date = LocalDateTime.now(),
        )
    )
}

@Preview(group = "Undone")
@Composable
fun PeopleReadingItSignPreview() {
    PeopleReadingItSign(
        peopleReadingIt = 56,
    )
}

@Preview(group = "Done")
@Composable
fun ISBNIconPreview() {
    CreateISBNIcon(type = ISBNType.ISBN13)
}

@Preview(group = "Done")
@Composable
fun GenreChipsPreview() {
    CreateGenresChips(
        genres = listOf(
            "Fantasy",
            "Science fiction",
            "Contemporary fiction",
            "Horror",
            "Fiction",
            "Middle Age"
        )
    )
}

@Preview(group = "Done")
@Composable
fun ReviewItemPreview() {
    ReviewItem(
        review = UserBookRating(
            id = "",
            rating = 4.5,
            review = "I love it.",
            book = null,
            user = Profile(
                id = "",
                firstName = "Paco",
                lastName = "Jones",
                bookProgress = emptyList(),
                giveUp = emptyList(),
                following = emptyList(),
                alreadyRead = emptyList(),
                userShelves = emptyList(),
            ),
            date = LocalDateTime.now(),
        )
    )
}

@Preview(group = "Undone")
@Composable
fun StartToReadSuccessDialogPreview() {
    SuccessStartToReadDialog(
        isLoading = false,
        bookTitle = "El imperio final",
        onClickButton = {}
    )
}