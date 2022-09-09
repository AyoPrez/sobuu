package com.ayoprez.sobuu.shared.features.comments.database

import android.content.SharedPreferences
import com.ayoprez.sobuu.shared.core.SessionTokenManager
import javax.inject.Inject

class CommentLocalDataImpl @Inject constructor(
    prefs: SharedPreferences
): ICommentLocalData, SessionTokenManager(prefs) {
    override suspend fun getSessionToken(): String? {
        return super.obtainSessionToken()
    }
}