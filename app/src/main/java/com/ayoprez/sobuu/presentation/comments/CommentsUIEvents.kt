package com.ayoprez.sobuu.presentation.comments

enum class CommentFilterType {
    ONLY_COMMENT_PEOPLE_I_KNOW,
    ONLY_COMMENTS_WITHOUT_SPOILERS,
    ONLY_COMMENTS_LAST_WEEK,
    ONLY_COMMENTS_LAST_MONTH,
}

sealed class CommentsUIEvents {
    data class FilterByType(val type: CommentFilterType): CommentsUIEvents()
    object displayFilterScreen: CommentsUIEvents()
    object displayCommentsScreen: CommentsUIEvents()
}
