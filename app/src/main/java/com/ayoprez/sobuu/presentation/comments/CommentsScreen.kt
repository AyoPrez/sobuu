package com.ayoprez.sobuu.presentation.comments

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.presentation.custom_widgets.MenuItemData
import com.ayoprez.sobuu.presentation.custom_widgets.TopAppBarWithMenu
import com.ayoprez.sobuu.presentation.main.toStringDateWithDayAndTime
import com.ayoprez.sobuu.shared.models.bo_models.Comment
import com.ayoprez.sobuu.ui.theme.DarkLava
import com.ayoprez.sobuu.ui.theme.SourceSans
import com.ayoprez.sobuu.ui.theme.WhiteBlue
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun CommentsScreen(
    nav: DestinationsNavigator? = null,
    commentsList: ArrayList<Comment>,
    viewModel: CommentsViewModel = hiltViewModel(),
) {
    val page = commentsList[0].pageNumber
    val percentage = commentsList[0].percentage

    Scaffold(
        topBar = {
            TopAppBarWithMenu(
                nav = nav,
                title = if (page != null) {
                    "${stringResource(id = R.string.comments_on_page)} $page"
                } else {
                    "${stringResource(id = R.string.comments_on_percentage)} $percentage%"
                },
                titleSize = 24.sp,
                listItems = listOf(
                    MenuItemData(
                        text = stringResource(id = R.string.write_comment),
                        icon = Icons.Filled.Message,
                        action = {}
                    )
                ),
                showCollapseMenu = false,
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .background(WhiteBlue)
            ) {
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(
                            top = 10.dp,
                            bottom = 10.dp,
                            start = 20.dp,
                            end = 20.dp
                        )
                        .align(Alignment.End)
                ) {
                    Icon(
                        modifier = Modifier
                            .width(36.dp)
                            .height(36.dp),
                        imageVector = if (viewModel.state.applyFilter) {
                            Icons.Filled.FilterAltOff
                        } else {
                            Icons.Filled.FilterAlt
                        },
                        contentDescription = null,
                        tint = DarkLava
                    )
                }

                CommentsInPage(
                    page = page,
                    percentage = percentage,
                    comments = commentsList,
                )
            }
        }
    )
}

@Composable
fun CommentsInPage(
    modifier: Modifier = Modifier,
    page: Int? = null,
    percentage: Byte? = null,
    comments: List<Comment>,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBlue)
            .padding(
                top = 10.dp,
                bottom = 20.dp,
                start = 20.dp,
                end = 20.dp
            )
            .then(modifier)
            .border(
                width = 2.dp,
                color = DarkLava
            ),
    ) {
        val listState = rememberLazyListState()

        LazyColumn(
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(comments.size) { index ->
                val comment = comments[index]
                CommentItem(
                    username = comment.username,
                    date = comment.publishedDate.toStringDateWithDayAndTime(),
                    text = comment.text,
                    votes = comment.votesCounter.toInt(),
                )
                Divider(
                    color = DarkLava.copy(alpha = 0.6f),
                    modifier = Modifier
                        .height(2.dp)
                        .width(60.dp)
                )
            }
        }

        Text(
            if (page != null) {
                "$page"
            } else {
                "$percentage%"
            },
            modifier = Modifier
                .align(if (page != null) Alignment.BottomEnd else Alignment.BottomStart)
                .padding(10.dp),
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = SourceSans,
                color = DarkLava
            )
        )
    }
}

@Composable
fun CommentItem(
    username: String,
    date: String,
    text: String,
    votes: Int,
) {

    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp.dp

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(WhiteBlue),
    ) {

        val (commentUsername, commentDate, commentText,
            commentVotes, commentSettings) = createRefs()
        val nameGuideline = createGuidelineFromStart(0.6f)

        Text(
            text = username,
            style = TextStyle(
                fontSize = 14.sp,
                color = DarkLava,
                fontFamily = SourceSans
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .width(screenWidth * 60 / 100)
                .constrainAs(commentUsername) {
                    top.linkTo(parent.top, margin = 10.dp)
                    linkTo(parent.start, nameGuideline, bias = 0f, startMargin = 10.dp)
                },
        )

        Text(
            text = date,
            style = TextStyle(
                fontSize = 14.sp,
                color = DarkLava,
                fontFamily = SourceSans
            ),
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.constrainAs(commentDate) {
                top.linkTo(parent.top, margin = 10.dp)
                end.linkTo(parent.end, margin = 10.dp)
            },
        )

        Text(
            text = text,
            style = TextStyle(
                fontSize = 18.sp,
                color = DarkLava,
                fontFamily = SourceSans
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.constrainAs(commentText) {
                top.linkTo(commentUsername.bottom, margin = 20.dp)
                linkTo(parent.start, parent.end, bias = 0f, startMargin = 10.dp, endMargin = 10.dp)
            },
        )

        Row(
            modifier = Modifier
                .width(screenWidth * 50 / 100)
                .height(Icons.Filled.ThumbUpAlt.defaultHeight)
                .constrainAs(commentVotes) {
                    top.linkTo(commentText.bottom, margin = 20.dp)
                    bottom.linkTo(parent.bottom, margin = 10.dp)
                    start.linkTo(parent.start)
                },
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.ThumbUpAlt,
                    contentDescription = null,
                    tint = DarkLava,
                )
            }
            Text(
                text = if (votes <= 999) {
                    "$votes"
                } else {
                    stringResource(id = R.string.more_than_999)
                },
                style = TextStyle(
                    fontSize = 16.sp,
                    color = DarkLava,
                    fontFamily = SourceSans
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 5.dp, end = 5.dp),
            )
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.ThumbDownAlt,
                    contentDescription = null,
                    tint = DarkLava,
                )
            }
        }

        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.constrainAs(commentSettings) {
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }
        ) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = null,
                tint = DarkLava,
            )
        }
    }
}

@Preview
@Composable
fun CommentsInPagePreview() {
    CommentsInPage(
        page = 33,
        comments = emptyList()
    )
}

@Preview
@Composable
fun CommentItemPreview() {
    CommentItem(
        username = "Ayoze Pérez Rodríguez <De las nieves extranjeras>",
        date = "Sep 17, 2022, 21:10",
        text = "It's good this part",
        votes = 220000,
    )
}