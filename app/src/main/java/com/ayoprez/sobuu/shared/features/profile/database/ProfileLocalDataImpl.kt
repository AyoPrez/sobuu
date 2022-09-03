package com.ayoprez.sobuu.shared.features.profile.database

import android.content.SharedPreferences
import com.ayoprez.sobuu.shared.core.SessionTokenManager
import javax.inject.Inject

class ProfileLocalDataImpl @Inject constructor(
    prefs: SharedPreferences
): IProfileLocalData, SessionTokenManager(prefs) {

    override suspend fun getSessionToken(): String? {
        return super.obtainSessionToken()
    }
}