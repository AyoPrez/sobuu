package com.ayoprez.sobuu.presentation.comments

enum class CommentFilterType {
    ONLY_COMMENT_PEOPLE_I_KNOW,
    ONLY_COMMENTS_WITHOUT_SPOILERS,
    ONLY_COMMENTS_LAST_WEEK,
    ONLY_COMMENTS_LAST_MONTH,
}

sealed class CommentsUIEvents {
    data class FilterByType(val type: CommentFilterType): CommentsUIEvents()
    data class DisplayCommentsScreen(val bookId: String, val page: Number? = null, val percentage: Number? = null ): CommentsUIEvents()
    object DisplayFilterScreen: CommentsUIEvents()
}
