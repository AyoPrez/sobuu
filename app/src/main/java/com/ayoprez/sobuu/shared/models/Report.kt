package com.ayoprez.sobuu.shared.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Reason to report:
 * -(0)Polemic comment
 * -(1)Use of ofensive language
 * -(2)Spam
 * -(3)Racism
 * -(4)Violence
 * -(5)Personal Details
 * -(6)Selling illegal products
 * -(7)Others
 */
enum class ReportReason(reason: Byte) {
    PolemicComment(0),
    UseOfOffensiveLanguage(1),
    Spam(2),
    Racism(3),
    Violence(4),
    PersonalDetails(5),
    SellingIllegalProducts(6),
    Others(7),
}

@Parcelize
data class Report(
    val id: String,
    val comment: Comment,
    val reason: ReportReason,
    val user: Profile,
) : Parcelable
