package com.ayoprez.sobuu.shared.models

import java.time.LocalDateTime

data class Comment(val id: String,
                   val user: Profile,
                   val publishedDate: LocalDateTime,
                   val text: String,
                   val hasSpoilers: Boolean,
                   val votesCounter: Long,
                   val percentage: Byte,
                   val pageNumber: Int,
                   val parentCommentId: String)
