package com.ayoprez.sobuu.presentation.main

import android.app.Activity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.presentation.authentication.login.LoginViewModel
import com.ayoprez.sobuu.presentation.custom_widgets.CustomBottomAppBar
import com.ayoprez.sobuu.presentation.custom_widgets.TopAppBarWithSearchAndProfile
import com.ayoprez.sobuu.presentation.destinations.CurrentlyReadingScreenDestination
import com.ayoprez.sobuu.presentation.destinations.HomeScreenDestination
import com.ayoprez.sobuu.presentation.destinations.LoginScreenDestination
import com.ayoprez.sobuu.shared.features.authentication.remote.AuthenticationResult
import com.ayoprez.sobuu.shared.features.book.remote.BookError
import com.ayoprez.sobuu.shared.models.bo_models.BookProgress
import com.ayoprez.sobuu.shared.models.bo_models.CurrentlyReadingBook
import com.ayoprez.sobuu.shared.models.bo_models.FinishedReadingBook
import com.ayoprez.sobuu.shared.models.bo_models.GiveUpBook
import com.ayoprez.sobuu.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class BookStatusType {
    CURRENTLY_READING, ALREADY_READ, GIVE_UP
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun HomeScreen(
    nav: DestinationsNavigator,
    loginViewModel: LoginViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val searchState = searchViewModel.state
    val activity = (LocalContext.current as? Activity)
    val focus = LocalFocusManager.current

    LaunchedEffect(loginViewModel, context) {
        loginViewModel.authResult.collect { result ->
            if (result == AuthenticationResult.LoggedOut<Unit>()) {
                nav.navigate(LoginScreenDestination) {
                    popUpTo(HomeScreenDestination.route) {
                        inclusive = true
                    }
                }
            }
        }
    }

    SobuuTheme(
        darkTheme = false,
    ) {
        BackPressHandler(onBackPressed = {
            if (!homeViewModel.state.isOnSearch) {
                activity?.finish()
            } else {
                if (searchState.searchTerm.isNotBlank()) {
                    searchViewModel.onEvent(
                        SearchUIEvent.cleanSearchTerm
                    )
                } else {
                    focus.clearFocus()
                }
            }
        })

        Scaffold(
            topBar = {
                TopAppBarWithSearchAndProfile(
                    nav = nav,
                    text = stringResource(id = R.string.app_name),
                    onSearchFieldValueChange = {
                        searchViewModel.onEvent(
                            SearchUIEvent.SearchTermChanged(it, Locale.current.language)
                        )
                    },
                    searchFieldValue = searchState.searchTerm,
                    onSearchButtonClick = {
                        searchViewModel.onEvent(
                            SearchUIEvent.searchTerm
                        )
                    },
                    clearTextButtonClick = {
                        searchViewModel.onEvent(
                            SearchUIEvent.cleanSearchTerm
                        )
                    },
                    onSearchFieldFocusChange = {
                        homeViewModel.onEvent(HomeUIEvent.DisplaySearch(it))
                    },
                )
            },
            content = {
                Content(
                    nav = nav,
                    modifier = Modifier.padding(it),
                    homeViewModel = homeViewModel
                )
            },
            bottomBar = { CustomBottomAppBar(nav = nav) }
        )
    }

}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    nav: DestinationsNavigator? = null,
    homeViewModel: HomeViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
            .then(modifier),
    ) {
        if (homeViewModel.state.isOnSearch) {
            SearchListScreen(nav = nav)
        } else {
            HomeContent(nav = nav, homeViewModel = homeViewModel)
        }
    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    nav: DestinationsNavigator? = null,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {

    Column {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .then(modifier),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            TextButton(onClick = {
                homeViewModel.onEvent(HomeUIEvent.DisplayCurrentlyReading)
            }) {
                Text(
                    text = stringResource(id = R.string.reading),
                    style = TextStyle(
                        color = if (homeViewModel.currentSection == BookStatusType.CURRENTLY_READING) DarkLava else SpanishGray,
                        fontSize = if (homeViewModel.currentSection == BookStatusType.CURRENTLY_READING) 20.sp else 14.sp,
                        fontFamily = SourceSans
                    )
                )
            }
            TextButton(onClick = {
                homeViewModel.onEvent(HomeUIEvent.DisplayFinished)
            }) {
                Text(
                    text = stringResource(id = R.string.already_read),
                    style = TextStyle(
                        color = if (homeViewModel.currentSection == BookStatusType.ALREADY_READ) DarkLava else SpanishGray,
                        fontSize = if (homeViewModel.currentSection == BookStatusType.ALREADY_READ) 20.sp else 14.sp,
                        fontFamily = SourceSans
                    )
                )
            }
            TextButton(onClick = {
                homeViewModel.onEvent(HomeUIEvent.DisplayGiveUp)
            }) {
                Text(
                    text = stringResource(id = R.string.give_up),
                    style = TextStyle(
                        color = if (homeViewModel.currentSection == BookStatusType.GIVE_UP) DarkLava else SpanishGray,
                        fontSize = if (homeViewModel.currentSection == BookStatusType.GIVE_UP) 20.sp else 14.sp,
                        fontFamily = SourceSans
                    )
                )
            }
        }

        if (homeViewModel.state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WhiteBlue),
                contentAlignment = Center,
            ) {
                CircularProgressIndicator(modifier = modifier, color = GreenSheen)
            }
        }

        if (homeViewModel.state.error == null) {
            when (homeViewModel.currentSection) {
                BookStatusType.CURRENTLY_READING -> {
                    SectionCurrentlyReading(
                        bookList = homeViewModel.currentlyReadingBooksList,
                        onClick = {
                            nav?.navigate(CurrentlyReadingScreenDestination(it))
                        }
                    )
                }
                BookStatusType.ALREADY_READ -> {
                    SectionAlreadyRead(bookList = homeViewModel.finishedBooksList)
                }
                BookStatusType.GIVE_UP -> {
                    SectionGiveUp(bookList = homeViewModel.giveUpBooksList)
                }
            }
        } else {
            when (homeViewModel.state.error) {
                is BookError.TimeOutError -> {
                    stringResource(id = R.string.error_timeout)
                }
                else -> {
                    stringResource(id = R.string.error_unknown)
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SectionCurrentlyReading(
    bookList: List<CurrentlyReadingBook>?,
    onClick: (book: CurrentlyReadingBook) -> Unit,
) {
    val pagerState = rememberPagerState()

    HorizontalPager(
        state = pagerState,
        count = bookList?.size ?: 0,
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBlue),
        contentPadding = PaddingValues(20.dp),
        itemSpacing = 10.dp
    ) { page ->
        val book = bookList?.get(page) ?: return@HorizontalPager

        BooksCarousel(
            modifier = Modifier.clickable { onClick.invoke(book) },
            picture = book.picture,
            progress = book.bookProgress?.progressInPercentage?.toInt() ?: 0,
            startedToRead = book.bookProgress?.startedToRead,
            finishedToRead = book.bookProgress?.finishedToRead,
            title = book.title,
            authors = book.authors,
            finished = false,
            giveUp = false,
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SectionAlreadyRead(bookList: List<FinishedReadingBook>?) {
    val pagerState = rememberPagerState()

    HorizontalPager(
        state = pagerState,
        count = bookList?.size ?: 0,
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBlue),
        contentPadding = PaddingValues(20.dp),
        itemSpacing = (-20).dp
    ) { page ->
        val book = bookList?.get(page) ?: return@HorizontalPager

        BooksCarousel(
            picture = book.picture,
            progress = book.bookProgress?.progressInPercentage?.toInt() ?: 0,
            startedToRead = book.bookProgress?.startedToRead,
            finishedToRead = book.bookProgress?.finishedToRead,
            title = book.title,
            authors = book.authors,
            finished = true,
            giveUp = false,
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SectionGiveUp(bookList: List<GiveUpBook>?) {
    val pagerState = rememberPagerState()

    HorizontalPager(
        state = pagerState,
        count = bookList?.size ?: 0,
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBlue),
        contentPadding = PaddingValues(20.dp),
        itemSpacing = (-20).dp
    ) { page ->
        val book = bookList?.get(page) ?: return@HorizontalPager

        BooksCarousel(
            picture = book.picture,
            progress = book.bookProgress?.progressInPercentage?.toInt() ?: 0,
            startedToRead = book.bookProgress?.startedToRead,
            finishedToRead = book.bookProgress?.finishedToRead,
            title = book.title,
            authors = book.authors,
            finished = false,
            giveUp = true,
        )
    }
}

@Composable
fun BooksCarousel(
    modifier: Modifier = Modifier,
    picture: String,
    progress: Int,
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
            .fillMaxHeight()
            .width(screenWidth - 70.dp)
            .border(
                border = BorderStroke(2.dp, color = DarkLava),
                shape = RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(DarkLava)
            .padding(10.dp)
            .then(modifier)
    ) {

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {

            val (bookCover, bookTitle, bookAuthors, startedDate,
                progressIndicator, endDate) = createRefs()

            createVerticalChain(bookTitle, bookAuthors, chainStyle = ChainStyle.Packed)

            AsyncImage(
                model = picture,
                placeholder = painterResource(id = R.drawable.ic_cover_placeholder),
                contentDescription = null,
                modifier = Modifier
                    .height(screenHeight / 3)
                    .constrainAs(bookCover) {
                        top.linkTo(parent.top, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        centerHorizontallyTo(parent)
                    },
            )
            ProgressIndicator(
                progress = progress,
                modifier = Modifier.constrainAs(progressIndicator) {
                    bottom.linkTo(bookCover.bottom, margin = 10.dp)
                    end.linkTo(bookCover.end, margin = 10.dp)
                }
            )
            Text(
                "${stringResource(id = R.string.started_book_on)} ${
                    startedToRead?.toStringDateWithDayAndTime() ?: LocalDateTime.now().toStringDateWithDayAndTime()
                }",
                style = TextStyle(
                    fontFamily = SourceSans,
                    fontSize = 12.sp,
                    color = WhiteBlue,
                ),
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 5.dp)
                    .constrainAs(startedDate) {
                        top.linkTo(bookCover.bottom)
                        start.linkTo(bookCover.start)
                    },
            )
            if (finishedToRead != null && (finished || giveUp)) {
                Text(
                    "${
                        if(finished) {
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
                    linkTo(startedDate.bottom, bookAuthors.top, bias = 0.90f)
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
        }
    }
}

@Composable
fun BackPressHandler(
    backPressedDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    onBackPressed: () -> Unit
) {
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    DisposableEffect(key1 = backPressedDispatcher) {
        backPressedDispatcher?.addCallback(backCallback)

        onDispose {
            backCallback.remove()
        }
    }
}

fun LocalDateTime.toStringDateWithDayAndTime(): String {
    if(this.isEqual(LocalDateTime.now())) {
        return "Today"
    }

    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy, HH:mm")
    return this.format(formatter)
}

@Composable
fun ProgressIndicator(modifier: Modifier = Modifier, progress: Int) {
    Box(
        modifier = Modifier
            .width(60.dp)
            .height(60.dp)
            .wrapContentSize(Center)
            .then(modifier)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .align(Center)
                .clip(CircleShape)
                .background(Vermilion.copy(alpha = 0.7f))
        )
        Box(
            modifier = Modifier
                .size(45.dp)
                .align(Center)
                .clip(CircleShape)
                .background(GreenSheen.copy(alpha = 0.7f))
        ) {
            Text(
                text = "${progress}%",
                modifier = Modifier.align(Center),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = SourceSans,
                    color = WhiteBlue
                )
            )
        }
    }
}

// region Previews
@Preview(showSystemUi = true, showBackground = true, group = "Done")
@Composable
fun ComposableHomeScreenTopBarPreview() {
    TopAppBarWithSearchAndProfile(
        null,
        "Sobuu",
        searchFieldValue = "",
        onSearchFieldValueChange = {},
        onSearchButtonClick = {},
        clearTextButtonClick = {},
        onSearchFieldFocusChange = {},
    )
}

@Preview(showSystemUi = true, showBackground = true, group = "Done")
@Composable
fun ComposableProgressIndicatorPreview() {
    ProgressIndicator(progress = 10)
}

@Preview(showSystemUi = true, showBackground = true, group = "Done")
@Composable
fun ComposableCurrentlyReadingPreview() {
    SectionCurrentlyReading(
        onClick = {},
        bookList = listOf(
            CurrentlyReadingBook(
                authors = listOf("J.R.R Tolkien"),
                title = "El señor de los anillos: La comunidad del anillo",
                id = "w98hidn",
                picture = "",
                bookProgress = BookProgress(
                    progressInPercentage = 10,
                    startedToRead = LocalDateTime.now(),
                    percentage = 10,
                    page = null,
                ),
            )
        )
    )
}

@Preview(showSystemUi = true, showBackground = true, group = "Done")
@Composable
fun ComposableHomeScreenContentPreview() {
    HomeContent()
}

@Preview(showSystemUi = true, showBackground = true, group = "Done")
@Composable
fun ComposableBottomNavigationBarPreview() {
    CustomBottomAppBar()
}
// endregion

